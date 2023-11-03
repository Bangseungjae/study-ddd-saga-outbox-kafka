package com.food.ordering.system.dataaccess.restaurant.repository

import com.food.ordering.system.dataaccess.restaurant.entity.RestaurantEntity
import com.food.ordering.system.dataaccess.restaurant.entity.RestaurantEntityId
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface RestaurantJpaRepository : JpaRepository<RestaurantEntity, RestaurantEntityId> {

    fun findByRestaurantIdAndProductIdIn(restaurantId: UUID, productIds: List<UUID>): List<RestaurantEntity>
}
