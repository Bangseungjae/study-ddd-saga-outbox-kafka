package com.food.ordering.system.order.service.domain

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher
import com.food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent
import com.food.ordering.system.order.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RestaurantApprovalMessageListenerImpl(
    private val orderApprovalSaga: OrderApprovalSaga,
    private val orderCancelledEventDomainEventPublisher: DomainEventPublisher<OrderCancelledEvent>
) : RestaurantApprovalResponseMessageListener {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun orderApproved(restaurantApprovalResponse: RestaurantApprovalResponse) {
        orderApprovalSaga.process(restaurantApprovalResponse)
        logger.info("Order is approved for order id: ${restaurantApprovalResponse.orderId}")
    }

    override fun orderRejected(restaurantApprovalResponse: RestaurantApprovalResponse) {
        val domainEvent = orderApprovalSaga.rollback(restaurantApprovalResponse)
        logger.info("Publishing order cancelled event for order id: ${restaurantApprovalResponse.orderId} " +
                "with failure messages: ${restaurantApprovalResponse.failureMessages.joinToString { "," }}")

        orderCancelledEventDomainEventPublisher.publish(domainEvent)
    }
}
