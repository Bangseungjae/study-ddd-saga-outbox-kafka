package com.food.ordering.system.order.service.dataaccess.restaurant.adapter

import com.food.ordering.system.dataaccess.restaurant.repository.RestaurantJpaRepository
import com.food.ordering.system.order.service.dataaccess.restaurant.mapper.RestaurantDataAccessMapper
import com.food.ordering.system.order.service.domain.entity.Restaurant
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository
import org.springframework.stereotype.Component

@Component
private class RestaurantRepositoryImpl(
    private val restaurantJpaRepository: RestaurantJpaRepository,
    private val restaurantDataAccessMapper: RestaurantDataAccessMapper,
) : RestaurantRepository{
    override fun findRestaurantInformation(restaurant: Restaurant): Restaurant? {
        val restaurantProducts = restaurantDataAccessMapper.restaurantToRestaurantProducts(restaurant)
        val restaurantEntities = restaurantJpaRepository.findByRestaurantIdAndProductIdIn(
            restaurantId = restaurant.id.value,
            productIds = restaurantProducts,
        )
        return restaurantDataAccessMapper.restaurantEntityToRestaurant(restaurantEntities)
    }
}
