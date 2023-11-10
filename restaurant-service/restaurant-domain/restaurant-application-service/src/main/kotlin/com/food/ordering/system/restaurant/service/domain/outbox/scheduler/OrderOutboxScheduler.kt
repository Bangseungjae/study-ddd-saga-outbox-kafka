package com.food.ordering.system.restaurant.service.domain.outbox.scheduler

import com.food.ordering.system.outbox.OutboxScheduler
import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.RestaurantApprovalResponseMessagePublisher
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class OrderOutboxScheduler(
    private val orderOutboxHelper: OrderOutboxHelper,
    private val responseMessagePublisher: RestaurantApprovalResponseMessagePublisher,
) : OutboxScheduler {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    @Scheduled(
        fixedRateString = "\${restaurant-service.outbox-scheduler-fixed-rate}",
        initialDelayString = "\${restaurant-service.outbox-scheduler-initial-delay}"
    )
    override fun processOutboxMessage() {
        val outboxMessages =
            orderOutboxHelper.getOrderOutboxMessageByOutboxStatus(OutboxStatus.STARTED)

        if (outboxMessages.isNotEmpty()) {
            logger.info("Received ${outboxMessages.size} OrderOutboxMessage with ids: ${
                outboxMessages.joinToString(separator = ",") { outboxMessage ->
                    outboxMessage.id.toString()
                }
            }, sending to message bus!")

            outboxMessages.forEach {
                responseMessagePublisher.publish(
                    orderOutboxMessage = it,
                    orderOutboxHelper::updateOutboxMessage,
                )
            }
            logger.info("${outboxMessages.size} OrderOutboxMessage sent to message bus!")
        }
    }
}
