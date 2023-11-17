package com.food.ordering.system.domain.event.payload

import com.fasterxml.jackson.annotation.JsonProperty

data class OrderApprovalEventProduct(
    @field:JsonProperty
    val id: String,
    @field:JsonProperty
    val quantity: Int,
) {
}
