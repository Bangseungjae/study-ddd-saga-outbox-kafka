package com.food.ordering.system.restaurant.service.domain.exception

import com.food.ordering.system.domain.exception.DomainException

class RestaurantApplicationServiceException(
    message: String = ""
) : DomainException(message) {
}
