package com.food.ordering.system.domain.event.payload

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.ZonedDateTime

data class PaymentOrderEventPayload(
    @field:JsonProperty
    val paymentId: String,
    @field:JsonProperty
    val customerId: String,
    @field:JsonProperty
    val orderId: String,
    @field:JsonProperty
    val price: BigDecimal,
    @field:JsonProperty
    val createdAt: ZonedDateTime,
    @field:JsonProperty
    val paymentStatus: String,
    @field:JsonProperty
    val failureMessages: List<String>,
)
