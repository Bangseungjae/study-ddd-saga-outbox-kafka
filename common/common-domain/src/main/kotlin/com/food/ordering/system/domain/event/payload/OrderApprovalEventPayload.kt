package com.food.ordering.system.order.service.domain.outbox.model.approval

import com.fasterxml.jackson.annotation.JsonProperty
import com.food.ordering.system.domain.valueobject.RestaurantOrderStatus
import java.math.BigDecimal
import java.time.ZonedDateTime

data class OrderApprovalEventPayload(
    @field:JsonProperty
    val orderId: String,
    @field:JsonProperty
    val restaurantId: String,
    @field:JsonProperty
    val price: BigDecimal,
    @field:JsonProperty
    val createdAt: ZonedDateTime,
    @field:JsonProperty
    val restaurantOrderStatus: String,
    @field:JsonProperty
    val products: List<OrderApprovalEventProduct>
) {
}
