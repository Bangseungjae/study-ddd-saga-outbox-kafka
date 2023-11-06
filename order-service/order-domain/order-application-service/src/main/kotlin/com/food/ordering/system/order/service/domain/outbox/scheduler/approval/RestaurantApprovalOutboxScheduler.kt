package com.food.ordering.system.order.service.domain.outbox.scheduler.approval

import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantapproval.RestaurantApprovalRequestMessagePublisher
import com.food.ordering.system.outbox.OutboxScheduler
import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.saga.SagaStatus
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class RestaurantApprovalOutboxScheduler(
    private val approvalOutboxHelper: ApprovalOutboxHelper,
    private val restaurantApprovalRequestMessagePublisher: RestaurantApprovalRequestMessagePublisher,
) : OutboxScheduler {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(
        fixedDelayString = "\${order-service.outbox-scheduler-fixed-rate}",
        initialDelayString = "\${order-service.outbox-scheduler-initial-delay}"
    )
    @Transactional
    override fun processOutboxMessage() {
        val outboxMessages = approvalOutboxHelper
            .getApprovalOutboxMessageByOutboxStatusAndSagaStatus(
                outboxStatus = OutboxStatus.STARTED,
                sagaStatus = arrayOf(SagaStatus.PROCESSING),
            )

        if (outboxMessages.isNotEmpty()) {
            logger.info("Received ${outboxMessages.size} OrderApprovalOutboxMessage with ids: ${
                outboxMessages.joinToString(separator = ",") { outboxMessage ->
                    outboxMessage.id.toString()
                }
            }, sending to message bus!")

            outboxMessages.forEach{ outboxMessage ->
                restaurantApprovalRequestMessagePublisher.publish(
                    orderApprovalOutboxMessage = outboxMessage,
                    outboxCallback = this::updateOutboxStatus
                )
            }
            logger.info("${outboxMessages.size} OrderApprovalOutboxMessage sent to message bus!")
        }

    }

    private fun updateOutboxStatus(
        orderApprovalOutboxMessage: OrderApprovalOutboxMessage,
        outboxStatus: OutboxStatus,
    ) {
        orderApprovalOutboxMessage.outboxStatus = outboxStatus
        approvalOutboxHelper.save(orderApprovalOutboxMessage)
        logger.info("OrderApprovalOutboxMessage is updated with outbox status: ${outboxStatus.name}")
    }
}
