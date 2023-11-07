package com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.repository

import com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.entity.ApprovalOutboxEntity
import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.saga.SagaStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ApprovalOutboxJpaRepository : JpaRepository<ApprovalOutboxEntity, UUID> {

    fun findByTypeAndOutboxStatusAndSagaStatusIn(
        type: String,
        outboxStatus: OutboxStatus,
        sagaStatus: List<SagaStatus>
    ): List<ApprovalOutboxEntity>

    fun findByTypeAndSagaIdAndSagaStatusIn(
        type: String,
        sagaId: UUID,
        sagaStatus: List<SagaStatus>,
    ): ApprovalOutboxEntity?

    fun deleteByTypeAndOutboxStatusAndSagaStatusIn(
        type: String,
        outboxStatus: OutboxStatus,
        sagaStatus: List<SagaStatus>,
    )
}
