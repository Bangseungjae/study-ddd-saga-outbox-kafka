package com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantapproval

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent

interface OrderPaidRestaurantRequestMessagePublisher : DomainEventPublisher<OrderPaidEvent> {
}
