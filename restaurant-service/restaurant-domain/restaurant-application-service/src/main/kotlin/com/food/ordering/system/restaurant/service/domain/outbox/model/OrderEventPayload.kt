package com.food.ordering.system.restaurant.service.domain.outbox.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.ZonedDateTime

class OrderEventPayload(
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
) {
}
