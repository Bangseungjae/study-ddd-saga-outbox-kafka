package com.food.ordering.system.order.service.domain

import com.food.ordering.system.domain.valueobject.PaymentStatus.COMPLETED
import com.food.ordering.system.order.service.dataaccess.outbox.payment.repository.PaymentOutboxJpaRepository
import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse
import com.food.ordering.system.saga.SagaStatus
import com.food.ordering.system.saga.order.ORDER_SAGA_NAME
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.test.context.jdbc.Sql
import java.math.BigDecimal
import java.time.Instant
import java.util.*
import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread

@SpringBootTest(classes = [OrderServiceApplication::class])
@Sql(value = ["classpath:sql/OrderPaymentSagaTestSetUp.sql"])
@Sql(
    value = ["classpath:sql/OrderPaymentSagaTestCleanUp.sql"],
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
class OrderPaymentSagaTest {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var orderPaymentSaga: OrderPaymentSaga

    @Autowired
    private lateinit var paymentOutboxJpaRepository: PaymentOutboxJpaRepository

    private val SAGA_ID = UUID.fromString("15a497c1-0f4b-4eff-b9f4-c402c8c07afa")
    private val ORDER_ID = UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb17")
    private val CUSTOMER_ID = UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb41")
    private val PAYMENT_ID = UUID.randomUUID()
    private val PRICE = BigDecimal(100)

    @Test
    fun testDoublePayment() {
        orderPaymentSaga.process(getPaymentResponse())
        orderPaymentSaga.process(getPaymentResponse())
    }

    @Test
    fun testDoublePaymentWithThreads() {
        val threads = arrayOf(
            thread(start = false) { orderPaymentSaga.process(getPaymentResponse()) },
            thread(start = false) { orderPaymentSaga.process(getPaymentResponse()) },
        )
        threads.forEach { it.start() }
        threads.forEach { it.join() }

        assertPaymentOutbox()
    }

    @Test
    fun testDoublePaymentWithLatch() {
        val latch = CountDownLatch(2)

        val thread1 = thread(start = false) {
            try {
                orderPaymentSaga.process(getPaymentResponse())
            } catch (e: OptimisticLockingFailureException) {
                logger.error("OptimisticLockingFailureException occurred for thread1")
            } finally {
                latch.countDown()
            }
        }

        val thread2 = thread(start = false) {
            try {
                orderPaymentSaga.process(getPaymentResponse())
            } catch (e: OptimisticLockingFailureException) {
                logger.error("OptimisticLockingFailureException occurred for thread1")
            } finally {
                latch.countDown()
            }
        }

        thread1.start()
        thread2.start()

        latch.await()

        assertPaymentOutbox()
    }

    private fun assertPaymentOutbox() {
        val paymentOutboxEntity = paymentOutboxJpaRepository.findByTypeAndSagaIdAndSagaStatusIn(
            type = ORDER_SAGA_NAME,
            sagaId = SAGA_ID,
            sagaStatus = listOf(SagaStatus.PROCESSING)
        )
        assertTrue(paymentOutboxEntity != null)
    }

    private fun getPaymentResponse(): PaymentResponse {
        return PaymentResponse(
            id = UUID.randomUUID().toString(),
            sagaId = SAGA_ID.toString(),
            paymentStatus = COMPLETED,
            paymentId = PAYMENT_ID.toString(),
            orderId = ORDER_ID.toString(),
            customerId = CUSTOMER_ID.toString(),
            price = PRICE,
            createdAt = Instant.now(),
            failureMessages = listOf()
        )
    }
}
