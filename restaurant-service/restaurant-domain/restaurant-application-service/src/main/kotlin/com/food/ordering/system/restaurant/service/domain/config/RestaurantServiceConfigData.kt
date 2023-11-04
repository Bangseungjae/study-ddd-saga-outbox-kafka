package com.food.ordering.system.restaurant.service.domain.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "restaurant-service")
data class RestaurantServiceConfigData(
    val restaurantApprovalRequestTopicName: String,
    val restaurantApprovalResponseTopicName: String,
) {
}
