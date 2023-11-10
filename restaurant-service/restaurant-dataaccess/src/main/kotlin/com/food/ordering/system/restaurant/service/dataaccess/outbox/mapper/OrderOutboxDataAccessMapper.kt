package com.food.ordering.system.restaurant.service.dataaccess.outbox.mapper

import com.food.ordering.system.restaurant.service.dataaccess.outbox.entity.OrderOutboxEntity
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderOutboxMessage

fun OrderOutboxMessage.toOutboxEntity(): OrderOutboxEntity = run {
    OrderOutboxEntity(
        id = id,
        sagaId = sagaId,
        createdAt = createdAt,
        processedAt = processedAt,
        type = type,
        payload = payload,
        outboxStatus = outboxStatus,
        approvalStatus = approvalStatus,
        version = version,
    )
}

fun OrderOutboxEntity.toOrderOutboxMessage() = run {
    OrderOutboxMessage(
        id = id,
        sagaId = sagaId,
        createdAt = createdAt,
        processedAt = processedAt,
        type = type,
        payload = payload,
        outboxStatus = outboxStatus,
        approvalStatus = approvalStatus,
        version = version,
    )
}

