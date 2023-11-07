package com.food.ordering.system.order.service.dataaccess.outbox.payment.mapper

import com.food.ordering.system.order.service.dataaccess.outbox.payment.entity.PaymentOutboxEntity
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage

fun OrderPaymentOutboxMessage.toOutboxEntity(): PaymentOutboxEntity = run {
    PaymentOutboxEntity(
        id = id,
        sagaId = sagaId,
        createdAt = createdAt,
        processedAt = createdAt,
        type = type,
        payload = payload,
        sagaStatus = sagaStatus,
        orderStatus = orderStatus,
        outboxStatus = outboxStatus,
        version = version,
    )
}

fun PaymentOutboxEntity.toOrderPaymentOutboxMessage(): OrderPaymentOutboxMessage = run {
    OrderPaymentOutboxMessage(
        id = id,
        sagaId = sagaId,
        createdAt = createdAt,
        processedAt = processedAt,
        type = type,
        payload = payload,
        sagaStatus = sagaStatus,
        orderStatus = orderStatus,
        outboxStatus = outboxStatus,
        version = version,
    )
}
