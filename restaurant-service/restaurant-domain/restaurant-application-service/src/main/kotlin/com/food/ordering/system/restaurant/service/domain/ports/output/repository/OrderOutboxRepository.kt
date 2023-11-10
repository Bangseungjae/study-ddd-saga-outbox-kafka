package com.food.ordering.system.restaurant.service.domain.ports.output.repository

import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderOutboxMessage
import java.util.UUID

interface OrderOutboxRepository {

    fun save(orderOutboxMessage: OrderOutboxMessage): OrderOutboxMessage

    fun findByTypeAndOutboxStatus(
        type: String,
        outboxStatus: OutboxStatus,
    ): List<OrderOutboxMessage>

    fun findByTypeAndSagaIdAndOutboxStatus(
        type: String,
        sagaId: UUID,
        outboxStatus: OutboxStatus,
    ): OrderOutboxMessage?

    fun deleteByTypeAndOutboxStatus(
        type: String,
        outboxStatus: OutboxStatus,
    )
}
