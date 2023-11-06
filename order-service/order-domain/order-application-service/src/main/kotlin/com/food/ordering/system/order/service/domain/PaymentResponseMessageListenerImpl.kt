package com.food.ordering.system.order.service.domain

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher
import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent
import com.food.ordering.system.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PaymentResponseMessageListenerImpl(
    private val orderPaymentSaga: OrderPaymentSaga,
    private val orderPaidEventDomainEventPublisher: OrderPaidRestaurantRequestMessagePublisher,
) : PaymentResponseMessageListener {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun paymentCompleted(paymentResponse: PaymentResponse) {
        val domainEvent = orderPaymentSaga.process(paymentResponse)
        logger.info("Publishing OrderPaidEvent for order id: ${paymentResponse.orderId}")
        orderPaidEventDomainEventPublisher.publish(domainEvent)

    }

    override fun paymentCancelled(paymentResponse: PaymentResponse) {
        orderPaymentSaga.rollback(paymentResponse)
        logger.info("Order is roll back for order id: ${paymentResponse.orderId} with failure messages: " +
                "${paymentResponse.failureMessages.joinToString { "," }}")
    }
}
