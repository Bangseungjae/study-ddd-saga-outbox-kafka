package com.food.ordering.system.payment.service.domain.dto

import com.food.ordering.system.domain.valueobject.CustomerId
import com.food.ordering.system.domain.valueobject.PaymentOrderStatus
import java.math.BigDecimal
import java.time.Instant

data class PaymentRequest(
    val id: String,
    val sataId: String,
    val orderId: String,
    val customerId: CustomerId,
    val price: BigDecimal,
    val createdAt: Instant,
    val paymentOrderStatus: PaymentOrderStatus,
)
