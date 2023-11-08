package com.food.ordering.system.payment.service.domain.outbox.model

import com.food.ordering.system.domain.valueobject.PaymentStatus
import com.food.ordering.system.outbox.OutboxStatus
import java.time.ZonedDateTime
import java.util.UUID

data class OrderOutboxMessage(
    val id: UUID,
    val sagaId: UUID,
    val createdAt: ZonedDateTime,
    val processedAt: ZonedDateTime,
    val type: String,
    val payload: String,
    var paymentStatus: PaymentStatus,
    var outboxStatus: OutboxStatus,
    val version: Int,
)
