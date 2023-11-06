package com.food.ordering.system.order.service.domain.outbox.scheduler.payment

import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.PaymentRequestMessagePublisher
import com.food.ordering.system.outbox.OutboxScheduler
import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.saga.SagaStatus
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class PaymentOutboxScheduler(
    private val paymentOutboxHelper: PaymentOutboxHelper,
    private val paymentRequestMessagePublisher: PaymentRequestMessagePublisher,
) : OutboxScheduler {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    @Scheduled(
        fixedDelayString = "\${order-service.outbox-scheduler-fixed-rate}",
        initialDelayString = "\${order-service.outbox-scheduler-initial-delay}"
    )
    override fun processOutboxMessage() {
        val outboxMessages =
            paymentOutboxHelper.getPaymentOutboxMessageByOutboxStatusAndSagaStatus(
                outboxStatus = OutboxStatus.STARTED,
                sagaStatus = arrayOf(SagaStatus.STARTED, SagaStatus.COMPENSATING),
            )
        if (outboxMessages.isNotEmpty()) {
            logger.info(
                "Received ${outboxMessages.size} OrderPaymentOutboxMessage with ids: ${
                    outboxMessages.joinToString(separator = ",") { outboxMessage ->
                        outboxMessage.id.toString()
                    }
                }, sending to message bus!"
            )
        }

        outboxMessages.forEach{ outboxMessage ->
            paymentRequestMessagePublisher.publish(
                orderPaymentOutboxMessage = outboxMessage,
                outboxCallback = this::updateOutboxStatus,
            )
        }
        logger.info("${outboxMessages.size} OrderPaymentOutboxMessage sent to message bus!")
    }
    private fun updateOutboxStatus(
        orderPaymentOutboxMessage: OrderPaymentOutboxMessage,
        outboxStatus: OutboxStatus,
    ) {
        orderPaymentOutboxMessage.outboxStatus = outboxStatus
        paymentOutboxHelper.save(orderPaymentOutboxMessage)
        logger.info("OrderPaymentOutboxMessage is updated with outbox status: ${outboxStatus.name}")
    }
}
