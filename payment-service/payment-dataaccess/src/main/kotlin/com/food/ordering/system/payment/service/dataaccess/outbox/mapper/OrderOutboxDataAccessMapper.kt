package com.food.ordering.system.payment.service.dataaccess.outbox.mapper

import com.food.ordering.system.payment.service.dataaccess.outbox.entity.OrderOutboxEntity
import com.food.ordering.system.payment.service.domain.outbox.model.OrderOutboxMessage

fun OrderOutboxMessage.toOutboxMessage(): OrderOutboxEntity = run {
    OrderOutboxEntity(
        id = id,
        sagaId = sagaId,
        createdAt = createdAt,
        processedAt = processedAt,
        type = type,
        payload = payload,
        outboxStatus = outboxStatus,
        paymentStatus = paymentStatus,
        version = version,
    )
}

fun OrderOutboxEntity.toOrderOutboxMessage(): OrderOutboxMessage = run {
    OrderOutboxMessage(
        id = id,
        sagaId = sagaId,
        createdAt = createdAt,
        processedAt = processedAt,
        type = type,
        payload = payload,
        paymentStatus = paymentStatus,
        outboxStatus = outboxStatus,
        version = version,
    )
}
