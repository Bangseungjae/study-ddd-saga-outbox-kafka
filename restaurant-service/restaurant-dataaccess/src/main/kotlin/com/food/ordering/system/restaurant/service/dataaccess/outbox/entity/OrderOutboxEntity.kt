package com.food.ordering.system.restaurant.service.dataaccess.outbox.entity

import com.food.ordering.system.domain.valueobject.OrderApprovalStatus
import com.food.ordering.system.outbox.OutboxStatus
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Version
import java.time.ZonedDateTime
import java.util.UUID

@Table(name = "order_outbox")
@Entity
class OrderOutboxEntity(
    @Id
    val id: UUID,
    val sagaId: UUID,
    val createdAt: ZonedDateTime,
    val processedAt: ZonedDateTime,
    val type: String,
    val payload: String,
    @Enumerated(EnumType.STRING)
    val outboxStatus: OutboxStatus,
    @Enumerated(EnumType.STRING)
    val approvalStatus: OrderApprovalStatus,
    @Version
    val version: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OrderOutboxEntity) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
