package com.food.ordering.system.restaurant.service.domain.ports.output.repository

import com.food.ordering.system.restaurant.service.domain.entity.Restaurant

interface RestaurantRepository {
    fun findRestaurantInformation(restaurant: Restaurant): Restaurant?
}
