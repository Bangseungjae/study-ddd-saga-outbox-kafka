package com.food.ordering.system.restaurant.service.domain.dto

import com.food.ordering.system.domain.valueobject.RestaurantOrderStatus
import com.food.ordering.system.restaurant.service.domain.entity.Product
import java.math.BigDecimal
import java.time.Instant

data class RestaurantApprovalRequest(
    val id: String,
    val sataId: String,
    val restaurantId: String,
    val orderId: String,
    val restaurantOrderStatus: RestaurantOrderStatus,
    val projects: List<Product>,
    val price: BigDecimal,
    val createdAt: Instant,
)
