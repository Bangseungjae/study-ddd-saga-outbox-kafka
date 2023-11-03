package com.food.ordering.system.dataaccess.restaurant.entity

import java.io.Serializable
import java.util.*

data class RestaurantEntityId(
    var restaurantId: UUID? = null,
    var productId: UUID? = null,
) : Serializable {
}
