package com.food.ordering.system.payment.service.domain.ports.output.output.message.publisher

import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.payment.service.domain.outbox.model.OrderOutboxMessage
import java.util.function.BiConsumer

interface PaymentResponseMessagePublisher {

    fun publish(
        orderOutboxMessage: OrderOutboxMessage,
        callback: BiConsumer<OrderOutboxMessage, OutboxStatus>,
    )
}
