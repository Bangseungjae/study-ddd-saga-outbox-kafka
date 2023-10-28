package com.food.ordering.system.order.service.domain.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "order-service")
data class OrderServiceConfigData @ConstructorBinding constructor(
    val paymentRequestTopicName: String,
    val paymentResponseTopicName: String,
    val restaurantApprovalRequestTopicName: String,
    val restaurantApprovalResponseTopicName: String,
)
