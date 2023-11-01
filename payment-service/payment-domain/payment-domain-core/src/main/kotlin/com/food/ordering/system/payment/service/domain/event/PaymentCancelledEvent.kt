package com.food.ordering.system.payment.service.domain.event

import com.food.ordering.system.payment.service.domain.entity.Payment
import java.time.ZonedDateTime

class PaymentCancelledEvent(
    payment: Payment,
    createdAt: ZonedDateTime,
    failureMessages: MutableList<String> = mutableListOf(),
) :  PaymentEvent(
    payment = payment,
    createdAt = createdAt,
    failureMessages = failureMessages
){
}
