package com.food.ordering.system.order.service.domain

import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent
import food.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener

@Component
class OrderCreatedEventApplicationListener(
    private val orderCreatedPaymentRequestMessagePublisher: OrderCreatedPaymentRequestMessagePublisher,
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @TransactionalEventListener
    fun process(orderCreatedEvent: OrderCreatedEvent) {
        orderCreatedPaymentRequestMessagePublisher.publish(orderCreatedEvent)
    }
}
