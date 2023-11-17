package com.food.ordering.system.domain.event.payload

import com.fasterxml.jackson.annotation.JsonProperty
import com.food.ordering.system.domain.valueobject.PaymentOrderStatus
import java.math.BigDecimal
import java.time.ZonedDateTime

data class OrderPaymentEventPayload(

    @field:JsonProperty
    val id: String? = null,
    @field:JsonProperty
    val sagaId: String,
    @field:JsonProperty
    val customerId: String,
    @field:JsonProperty
    val orderId: String,
    @field:JsonProperty
    val price: BigDecimal,
    @field:JsonProperty
    val createdAt: ZonedDateTime,
    @field:JsonProperty
    val paymentOrderStatus: String,
)
