package com.food.ordering.system.order.service.domain.outbox.model.payment

import com.food.ordering.system.domain.KOREA_DATE_TIME
import com.food.ordering.system.domain.valueobject.OrderStatus
import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.saga.SagaStatus
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

data class OrderPaymentOutboxMessage(
    val id: UUID,
    val sagaId: UUID,
    val createdAt: ZonedDateTime,
    var processedAt: ZonedDateTime = ZonedDateTime.now(ZoneId.of(KOREA_DATE_TIME)),
    val type: String,
    val payload: String,
    var sagaStatus: SagaStatus,
    var orderStatus: OrderStatus,
    var outboxStatus: OutboxStatus,
    var version: Int = 0,
) {
}
