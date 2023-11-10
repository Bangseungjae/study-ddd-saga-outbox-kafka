package com.food.ordering.system.restaurant.service.domain.event

import com.food.ordering.system.domain.valueobject.RestaurantId
import com.food.ordering.system.restaurant.service.domain.entity.OrderApproval
import java.time.ZonedDateTime

class OrderApprovedEvent(
    orderApproval: OrderApproval,
    restaurantId: RestaurantId,
    failureMessages: MutableList<String>,
    createdAt: ZonedDateTime,
) : OrderApprovalEvent(
    orderApproval,
    restaurantId,
    failureMessages,
    createdAt
) {
}
