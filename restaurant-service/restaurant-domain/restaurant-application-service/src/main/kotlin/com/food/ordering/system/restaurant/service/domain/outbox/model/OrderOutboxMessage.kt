package com.food.ordering.system.restaurant.service.domain.outbox.model

import com.food.ordering.system.domain.valueobject.OrderApprovalStatus
import com.food.ordering.system.outbox.OutboxStatus
import java.time.ZonedDateTime
import java.util.UUID

class OrderOutboxMessage(
    val id: UUID,
    val sagaId: UUID,
    val createdAt: ZonedDateTime,
    val processedAt: ZonedDateTime,
    val type: String,
    val payload: String,
    var outboxStatus: OutboxStatus,
    val approvalStatus: OrderApprovalStatus,
    val version: Int,
) {
}
