package com.food.ordering.system.payment.service.domain

import com.food.ordering.system.domain.valueobject.PaymentOrderStatus
import com.food.ordering.system.domain.valueobject.PaymentStatus
import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.payment.service.dataaccess.outbox.repository.OrderOutboxJpaRepository
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest
import com.food.ordering.system.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener
import com.food.ordering.system.saga.order.ORDER_SAGA_NAME
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataAccessException
import java.math.BigDecimal
import java.time.Instant
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors

@SpringBootTest(classes = [PaymentServiceApplication::class])
class PaymentRequestMessageListenerTest {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var paymentRequestMessageListener: PaymentRequestMessageListener

    @Autowired
    private lateinit var orderOutboxJpaRepository: OrderOutboxJpaRepository

    private val CUSTOMER_ID = "d215b5f8-0249-4dc5-89a3-51fd148cfb41"
    private val PRICE = BigDecimal("100")

    @Test
    fun testDoublePayment() {
        val sagaId = UUID.randomUUID().toString()
        paymentRequestMessageListener.completePayment(getPaymentRequest(sagaId))
        try {
            paymentRequestMessageListener.completePayment(getPaymentRequest(sagaId))
        } catch (e: DataAccessException) {
            logger.error("DataAccessException occurred with sql state: ${e.message}")
        }
        assertOrderOutbox(sagaId)

    }

    @Test
    fun testDoublePaymentWithThreads() {
        val sagaId = UUID.randomUUID().toString()

        val executor = Executors.newFixedThreadPool(2)
        try {
            val tasks = mutableListOf<Callable<Any>>()

            tasks.add(Executors.callable {
                try {
                    paymentRequestMessageListener.completePayment(getPaymentRequest(sagaId))
                } catch (e: DataAccessException) {
                    logger.error("DataAccessException occurred for thread 1 with sql state: ${e.rootCause?.message}")
                }
            })
            tasks.add(Executors.callable {
                try {
                    paymentRequestMessageListener.completePayment(getPaymentRequest(sagaId))
                } catch (e: DataAccessException) {
                    logger.error("DataAccessException occurred for thread 2 with sql state: ${e.rootCause?.message}")
                }
            })

            executor.invokeAll(tasks)

            assertOrderOutbox(sagaId)
        } catch (e: InterruptedException) {
            logger.error("Error calling complete payment!", e)
        } finally {
            executor.shutdown()
        }
    }

    private fun assertOrderOutbox(sagaId: String) {
        val orderOutboxEntity =
            orderOutboxJpaRepository.findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(
                type = ORDER_SAGA_NAME,
                sagaId = UUID.fromString(sagaId),
                paymentStatus = PaymentStatus.COMPLETED,
                outboxStatus = OutboxStatus.STARTED,
            )
        assertTrue(orderOutboxEntity != null)
        assertEquals(orderOutboxEntity?.sagaId.toString(), sagaId)
    }

    private fun getPaymentRequest(sagaId: String): PaymentRequest {
        return PaymentRequest(
            id = UUID.randomUUID().toString(),
            sagaId = sagaId,
            orderId = UUID.randomUUID().toString(),
            customerId = CUSTOMER_ID,
            paymentOrderStatus = PaymentOrderStatus.PENDING,
            price = PRICE,
            createdAt = Instant.now(),
        )
    }
}
