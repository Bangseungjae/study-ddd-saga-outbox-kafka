package com.food.ordering.system.restaurant.service.domain.event

import com.food.ordering.system.domain.event.DomainEvent
import com.food.ordering.system.domain.valueobject.RestaurantId
import com.food.ordering.system.restaurant.service.domain.entity.OrderApproval
import java.time.ZonedDateTime

abstract class OrderApprovalEvent(
    val orderApproval: OrderApproval,
    val restaurantId: RestaurantId,
    val failureMessages: MutableList<String>,
    val createdAt: ZonedDateTime,
) : DomainEvent<OrderApproval> {

    open fun fire() {}
}

