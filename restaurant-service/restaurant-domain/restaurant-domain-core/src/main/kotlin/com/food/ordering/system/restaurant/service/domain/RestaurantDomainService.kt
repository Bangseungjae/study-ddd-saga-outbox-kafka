package com.food.ordering.system.restaurant.service.domain

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher
import com.food.ordering.system.restaurant.service.domain.entity.Restaurant
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovalEvent
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovedEvent
import com.food.ordering.system.restaurant.service.domain.event.OrderRejectedEvent

interface RestaurantDomainService {
    fun validateOrder(
        restaurant: Restaurant,
        failureMessages: MutableList<String>,
        orderApprovedEventDomainEventPublisher: DomainEventPublisher<OrderApprovedEvent>,
        orderRejectedEventDomainEventPublisher: DomainEventPublisher<OrderRejectedEvent>,
    ): OrderApprovalEvent
}
