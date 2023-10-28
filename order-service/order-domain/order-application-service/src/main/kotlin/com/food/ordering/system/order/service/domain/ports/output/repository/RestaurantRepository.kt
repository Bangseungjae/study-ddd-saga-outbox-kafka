package com.food.ordering.system.order.service.domain.ports.output.repository

import com.food.ordering.system.order.service.domain.entity.Restaurant

interface RestaurantRepository {

    fun findRestaurantInformation(restaurant: Restaurant): Restaurant?
}
