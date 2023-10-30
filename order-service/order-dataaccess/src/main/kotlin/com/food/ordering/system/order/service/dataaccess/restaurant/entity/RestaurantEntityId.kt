package com.food.ordering.system.order.service.dataaccess.restaurant.entity

import java.io.Serializable
import java.util.*

data class RestaurantEntityId(
    var restaurantId: UUID? = null,
    var productId: UUID? = null,
) : Serializable {
}
