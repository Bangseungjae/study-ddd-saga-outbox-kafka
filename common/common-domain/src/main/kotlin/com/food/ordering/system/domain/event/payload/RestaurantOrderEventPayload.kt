package com.food.ordering.system.domain.event.payload

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.ZonedDateTime

data class RestaurantOrderEventPayload(
    @field:JsonProperty
    val orderId: String,
    @field:JsonProperty
    val restaurantId: String,
    @field:JsonProperty
    val createdAt: ZonedDateTime,
    @field:JsonProperty
    val orderApprovalStatus: String,
    @field:JsonProperty
    val failureMessages: List<String>,
)
