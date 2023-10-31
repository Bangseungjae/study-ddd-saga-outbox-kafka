package com.food.ordering.system.payment.service.domain.event

import com.food.ordering.system.domain.event.DomainEvent
import com.food.ordering.system.payment.service.domain.entity.Payment
import java.time.ZonedDateTime

abstract class PaymentEvent(
    val payment: Payment,
    val createdAt: ZonedDateTime,
    val failureMessages: MutableList<String>,
) : DomainEvent<Payment> {
}
