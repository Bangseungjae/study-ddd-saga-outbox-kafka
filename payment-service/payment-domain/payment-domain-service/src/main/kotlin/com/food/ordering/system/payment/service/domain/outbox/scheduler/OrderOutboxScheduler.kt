package com.food.ordering.system.payment.service.domain.outbox.scheduler

import com.food.ordering.system.outbox.OutboxScheduler
import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.payment.service.domain.ports.output.output.message.publisher.PaymentResponseMessagePublisher
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class OrderOutboxScheduler (
    private val orderOutboxHelper: OrderOutboxHelper,
    private val paymentResponseMessagePublisher: PaymentResponseMessagePublisher,
) : OutboxScheduler {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    @Scheduled(
        fixedRateString = "\${payment-service.outbox-scheduler-fixed-rate}",
        initialDelayString = "\${payment-service.outbox-scheduler-initial-delay}"
    )
    override fun processOutboxMessage() {
        val outboxMessageResponse =
            orderOutboxHelper.getOrderOutboxMessageByOutboxStatus(OutboxStatus.STARTED)

        if (outboxMessageResponse.isNotEmpty()) {
            logger.info("Received ${outboxMessageResponse.size} OrderOutboxMessage with ids:  ${
                outboxMessageResponse.joinToString(",") { it.id.toString() }
            }, sending to kafka!")
        }
        outboxMessageResponse.forEach {orderOutboxMessage ->
            paymentResponseMessagePublisher.publish(
                orderOutboxMessage = orderOutboxMessage,
                callback = orderOutboxHelper::updateOutboxMessage
            )
        }
        logger.info("${outboxMessageResponse.size} OrderOutboxMessage sent to message bus!")
    }
}
