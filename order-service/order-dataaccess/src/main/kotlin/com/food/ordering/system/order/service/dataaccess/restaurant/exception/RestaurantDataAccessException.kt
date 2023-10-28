package com.food.ordering.system.order.service.dataaccess.restaurant.exception

class RestaurantDataAccessException(
    override val message: String = "",
) : RuntimeException() {
}
