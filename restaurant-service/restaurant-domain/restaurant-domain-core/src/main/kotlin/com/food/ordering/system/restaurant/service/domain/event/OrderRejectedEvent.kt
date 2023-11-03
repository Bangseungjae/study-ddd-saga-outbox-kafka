package com.food.ordering.system.restaurant.service.domain.event

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher
import com.food.ordering.system.domain.valueobject.RestaurantId
import com.food.ordering.system.restaurant.service.domain.entity.OrderApproval
import java.time.ZonedDateTime

class OrderRejectedEvent(
    orderApproval: OrderApproval,
    restaurantId: RestaurantId,
    failureMessages: MutableList<String>,
    createdAt: ZonedDateTime,
    private val orderRejectedEventDomainEventPublisher: DomainEventPublisher<OrderRejectedEvent>
) : OrderApprovalEvent(
    orderApproval,
    restaurantId,
    failureMessages,
    createdAt
) {
    override fun fire() {
        orderRejectedEventDomainEventPublisher.publish(this)
    }
}
