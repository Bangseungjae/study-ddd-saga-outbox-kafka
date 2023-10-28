package com.food.ordering.system.order.service.dataaccess.restaurant.entity

import java.io.Serializable
import java.util.*

class RestaurantEntityId(
    val restaurantId: UUID,
    val productId: UUID,
) : Serializable {
}
