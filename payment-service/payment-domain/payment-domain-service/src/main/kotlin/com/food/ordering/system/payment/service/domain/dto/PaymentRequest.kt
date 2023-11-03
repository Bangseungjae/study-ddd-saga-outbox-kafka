package com.food.ordering.system.payment.service.domain.dto

import com.food.ordering.system.domain.valueobject.PaymentOrderStatus
import java.math.BigDecimal
import java.time.Instant

data class PaymentRequest(
    val id: String,
    val sagaId: String,
    val orderId: String,
    val customerId: String,
    val price: BigDecimal,
    val createdAt: Instant,
    val paymentOrderStatus: PaymentOrderStatus,
)
