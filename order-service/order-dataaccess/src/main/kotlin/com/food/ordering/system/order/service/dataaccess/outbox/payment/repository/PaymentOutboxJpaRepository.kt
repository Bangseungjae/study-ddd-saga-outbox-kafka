package com.food.ordering.system.order.service.dataaccess.outbox.payment.repository

import com.food.ordering.system.order.service.dataaccess.outbox.payment.entity.PaymentOutboxEntity
import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.saga.SagaStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PaymentOutboxJpaRepository : JpaRepository<PaymentOutboxEntity, UUID> {

    fun findByTypeAndOutboxStatusAndSagaStatusIn(
        type: String,
        outboxStatus: OutboxStatus,
        sagaStatus: List<SagaStatus>
    ): List<PaymentOutboxEntity>

    fun findByTypeAndSagaIdAndSagaStatusIn(
        type: String,
        sagaId: UUID,
        sagaStatus: List<SagaStatus>,
    ): PaymentOutboxEntity?

    fun deleteByTypeAndOutboxStatusAndSagaStatusIn(
        type: String,
        outboxStatus: OutboxStatus,
        sagaStatus: List<SagaStatus>,
    )
}
