package com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.entity

import com.food.ordering.system.domain.valueobject.OrderStatus
import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.saga.SagaStatus
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Version
import java.time.ZonedDateTime
import java.util.UUID

@Table(name = "restaurant_approval_outbox")
@Entity
class ApprovalOutboxEntity(
    @Id
    val id: UUID,
    val sagaId: UUID,
    val createdAt: ZonedDateTime,
    val processedAt: ZonedDateTime,
    val type: String,
    val payload: String,

    @Enumerated(EnumType.STRING)
    val sagaStatus: SagaStatus,

    @Enumerated(EnumType.STRING)
    val orderStatus: OrderStatus,

    @Enumerated(EnumType.STRING)
    val outboxStatus: OutboxStatus,

    @Version
    val version: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ApprovalOutboxEntity) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
