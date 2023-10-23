package com.food.ordering.system.order.service.domain

import com.food.ordering.system.order.service.domain.entity.Order
import com.food.ordering.system.order.service.domain.entity.Restaurant
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent

interface OrderDomainService {

    fun validateAndInitiateOrder(
        order: Order,
        restaurant: Restaurant,
    ): OrderCreatedEvent

    fun payOrder(order: Order): OrderPaidEvent

    fun approveOrder(order: Order)

    fun cancelOrderPayment(
        order: Order,
        failureMessage: List<String>,
    ): OrderCancelledEvent

    fun cancelOrder(
        order: Order,
        failureMessage: List<String>
    )
}
