package com.food.ordering.system.order.service.domain.outbox.scheduler.approval

import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage
import com.food.ordering.system.outbox.OutboxScheduler
import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.saga.SagaStatus
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class RestaurantApprovalOutboxCleanerScheduler(
    private val approvalOutboxHelper: ApprovalOutboxHelper,
) : OutboxScheduler {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    @Scheduled(cron = "@midnight")
    override fun processOutboxMessage() {
        val outboxMessages =
            approvalOutboxHelper.getApprovalOutboxMessageByOutboxStatusAndSagaStatus(
                outboxStatus = OutboxStatus.COMPLETED,
                sagaStatus = arrayOf(
                    SagaStatus.SUCCEEDED,
                    SagaStatus.FAILED,
                    SagaStatus.COMPENSATED,
                )
            )
        logger.info(
            "Received ${outboxMessages.size} OrderApprovalOutboxMessage for clean-up payloads: " +
                    outboxMessages.joinToString(separator = "\n") { it.payload }
        )
        approvalOutboxHelper.deleteApprovalOutboxMessageByOutboxStatusAndSagaStatus(
            outboxStatus = OutboxStatus.COMPLETED,
            sagaStatus = arrayOf(
                SagaStatus.SUCCEEDED,
                SagaStatus.FAILED,
                SagaStatus.COMPENSATED,
            )
        )
        logger.info("${outboxMessages.size} OrderApprovalOutboxMessage deleted!")
    }
}
