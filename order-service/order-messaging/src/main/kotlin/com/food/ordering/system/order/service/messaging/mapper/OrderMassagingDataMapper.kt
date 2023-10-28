package com.food.ordering.system.order.service.messaging.mapper

import com.food.ordering.system.kafka.order.avro.model.PaymentOrderStatus
import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class OrderMassagingDataMapper {
    fun orderCreatedEventToPaymentRequestAvroModel(orderCreatedEvent: OrderCreatedEvent) {
        val order = orderCreatedEvent.order
        PaymentRequestAvroModel(
            UUID.randomUUID().toString(),
            "",
            order.customerId.value.toString(),
            order.id.value.toString(),
            order.price.amount,
            orderCreatedEvent.createdAt.toInstant(),
            PaymentOrderStatus.PENDING
        )
    }

    fun orderCancelledEventToPaymentRequestAvroModel(orderCancelledEvent: OrderCancelledEvent) {
        val order = orderCancelledEvent.order
        PaymentRequestAvroModel(
            UUID.randomUUID().toString(),
            "",
            order.customerId.value.toString(),
            order.id.value.toString(),
            order.price.amount,
            orderCancelledEvent.createdAt.toInstant(),
            PaymentOrderStatus.CANCELLED
        )
    }

}
