package com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.adapter

import com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.mapper.toOrderApprovalOutboxMessage
import com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.mapper.toOutboxEntity
import com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.repository.ApprovalOutboxJpaRepository
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage
import com.food.ordering.system.order.service.domain.ports.output.repository.ApprovalOutboxRepository
import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.saga.SagaStatus
import org.springframework.stereotype.Component
import java.util.*

@Component
private class ApprovalOutboxRepositoryImpl(
    private val approvalOutboxJpaRepository: ApprovalOutboxJpaRepository,
) : ApprovalOutboxRepository {
    override fun save(orderApprovalOutboxMessage: OrderApprovalOutboxMessage): OrderApprovalOutboxMessage {
        return approvalOutboxJpaRepository.save(orderApprovalOutboxMessage.toOutboxEntity())
            .toOrderApprovalOutboxMessage()
    }

    override fun findByTypeAndOutboxStatusAndSagaStatus(
        type: String,
        outboxStatus: OutboxStatus,
        vararg sagaStatus: SagaStatus,
    ): List<OrderApprovalOutboxMessage> {
        return approvalOutboxJpaRepository.findByTypeAndOutboxStatusAndSagaStatusIn(
            type = type,
            outboxStatus = outboxStatus,
            sagaStatus = sagaStatus.toList(),
        ).map { it.toOrderApprovalOutboxMessage() }
            .toList()
    }

    override fun findByTypeAndSagaIdAndSagaStatus(
        type: String,
        sagaId: UUID,
        vararg sagaStatus: SagaStatus,
    ): OrderApprovalOutboxMessage? {
        return approvalOutboxJpaRepository.findByTypeAndSagaIdAndSagaStatusIn(
            type = type,
            sagaId = sagaId,
            sagaStatus = sagaStatus.toList()
        )?.toOrderApprovalOutboxMessage()
    }

    override fun deleteByTypeAndOutboxStatusAndSagaStatus(
        type: String,
        outboxStatus: OutboxStatus,
        vararg sagaStatus: SagaStatus,
    ) {
        approvalOutboxJpaRepository.deleteByTypeAndOutboxStatusAndSagaStatusIn(
            type = type,
            outboxStatus = outboxStatus,
            sagaStatus = sagaStatus.toList(),
        )
    }
}
