package com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.mapper

import com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.entity.ApprovalOutboxEntity
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage

fun OrderApprovalOutboxMessage.toOutboxEntity(): ApprovalOutboxEntity = ApprovalOutboxEntity(
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

fun ApprovalOutboxEntity.toOrderApprovalOutboxMessage(): OrderApprovalOutboxMessage = OrderApprovalOutboxMessage(
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
