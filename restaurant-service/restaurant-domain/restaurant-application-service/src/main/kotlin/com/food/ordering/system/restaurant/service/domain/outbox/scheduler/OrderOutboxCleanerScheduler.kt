package com.food.ordering.system.restaurant.service.domain.outbox.scheduler

import com.food.ordering.system.outbox.OutboxScheduler
import com.food.ordering.system.outbox.OutboxStatus
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class OrderOutboxCleanerScheduler(
    private val orderOutboxHelper: OrderOutboxHelper,
) : OutboxScheduler {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    @Scheduled(cron = "@midnight")
    override fun processOutboxMessage() {
        val outboxMessages =
            orderOutboxHelper.getOrderOutboxMessageByOutboxStatus(OutboxStatus.COMPLETED)
        if (outboxMessages.isNotEmpty()) {
            logger.info("Received ${outboxMessages.size} OrderMessage for clean-up!")
            orderOutboxHelper.deleteOrderOutboxMessageByOutboxStatus(OutboxStatus.COMPLETED)
            logger.info("Deleted ${outboxMessages.size} OrderOutboxMessage!")
        }
    }
}
