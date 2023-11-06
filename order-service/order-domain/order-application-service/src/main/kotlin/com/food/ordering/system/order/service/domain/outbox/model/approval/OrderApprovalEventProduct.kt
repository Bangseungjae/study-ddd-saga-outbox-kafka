package com.food.ordering.system.order.service.domain.outbox.model.approval

import com.fasterxml.jackson.annotation.JsonProperty

data class OrderApprovalEventProduct(
    @field:JsonProperty
    val id: String,
    @field:JsonProperty
    val quantity: Int,
) {
}
