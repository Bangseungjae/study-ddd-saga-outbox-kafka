package com.food.ordering.system.restaurant.service.dataaccess.restaurant.adapter

import com.food.ordering.system.dataaccess.restaurant.repository.RestaurantJpaRepository
import com.food.ordering.system.restaurant.service.dataaccess.restaurant.mapper.toRestaurant
import com.food.ordering.system.restaurant.service.dataaccess.restaurant.mapper.toRestaurantProducts
import com.food.ordering.system.restaurant.service.domain.entity.Restaurant
import com.food.ordering.system.restaurant.service.domain.ports.output.repository.RestaurantRepository
import org.springframework.stereotype.Component

@Component
class RestaurantRepositoryImpl(
    private val restaurantJpaRepository: RestaurantJpaRepository,
) : RestaurantRepository {
    override fun findRestaurantInformation(restaurant: Restaurant): Restaurant? {
        val restaurantProducts = restaurant.toRestaurantProducts()
        val restaurantEntities = restaurantJpaRepository.findByRestaurantIdAndProductIdIn(
            restaurantId = restaurant.id.value,
            productIds = restaurantProducts,
        )
        return restaurantEntities.toRestaurant()
    }
}
