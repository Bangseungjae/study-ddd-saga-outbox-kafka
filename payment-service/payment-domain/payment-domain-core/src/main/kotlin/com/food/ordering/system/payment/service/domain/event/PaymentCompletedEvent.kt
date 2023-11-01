package com.food.ordering.system.payment.service.domain.event

import com.food.ordering.system.payment.service.domain.entity.Payment
import java.time.ZonedDateTime

class PaymentCompletedEvent(
    payment: Payment,
    createdAt: ZonedDateTime,
) : PaymentEvent(
    payment = payment,
    createdAt = createdAt,
    failureMessages = mutableListOf()
) {
}
