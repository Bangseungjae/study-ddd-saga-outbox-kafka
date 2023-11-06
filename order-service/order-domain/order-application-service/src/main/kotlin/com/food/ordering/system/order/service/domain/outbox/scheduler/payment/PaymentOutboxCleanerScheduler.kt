package com.food.ordering.system.order.service.domain.outbox.scheduler.payment

import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage
import com.food.ordering.system.outbox.OutboxScheduler
import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.saga.SagaStatus
import com.food.ordering.system.saga.SagaStatus.*
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class PaymentOutboxCleanerScheduler(
    private val paymentOutboxHelper: PaymentOutboxHelper,
) : OutboxScheduler {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(cron = "@midnight")
    override fun processOutboxMessage() {
        val outboxMessages: List<OrderPaymentOutboxMessage> =
            paymentOutboxHelper.getPaymentOutboxMessageByOutboxStatusAndSagaStatus(
                outboxStatus = OutboxStatus.COMPLETED,
                sagaStatus = arrayOf(
                    SUCCEEDED,
                    FAILED,
                    COMPENSATED
                )
            )

        logger.info("Received ${outboxMessages.size} OrderPaymentOutboxMessage for clean-up. The payloads: ${
            outboxMessages.joinToString(separator = "\n") { it.payload }
        }")
        paymentOutboxHelper.deletePaymentOutboxMessageByOutboxStatusAndSagaStatus(
            outboxStatus = OutboxStatus.COMPLETED,
            sagaStatus = arrayOf(
                SUCCEEDED,
                FAILED,
                COMPENSATED
            )
        )
        logger.info("${outboxMessages.size} OrderPaymentOutboxMessage deleted!")
    }
}
