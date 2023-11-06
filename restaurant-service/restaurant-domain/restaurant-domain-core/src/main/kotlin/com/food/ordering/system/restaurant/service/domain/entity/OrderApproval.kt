package com.food.ordering.system.restaurant.service.domain.entity

import com.food.ordering.system.domain.entity.BaseEntity
import com.food.ordering.system.domain.valueobject.OrderApprovalStatus
import com.food.ordering.system.domain.valueobject.OrderId
import com.food.ordering.system.domain.valueobject.RestaurantId
import com.food.ordering.system.restaurant.service.domain.valueobject.OrderApprovalId

class OrderApproval(
    id: OrderApprovalId,
    val restaurantId: RestaurantId,
    val orderId: OrderId,
    var orderApprovalStatus: OrderApprovalStatus,
) : BaseEntity<OrderApprovalId>(id) {

}
