package com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher

import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderOutboxMessage
import java.util.function.BiConsumer

interface RestaurantApprovalResponseMessagePublisher {

    fun publish(
        orderOutboxMessage: OrderOutboxMessage,
        outboxCallback: BiConsumer<OrderOutboxMessage, OutboxStatus>
    )
}
