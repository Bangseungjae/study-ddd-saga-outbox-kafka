package com.food.ordering.system.payment.service.domain.exception

import com.food.ordering.system.domain.exception.DomainException

class PaymentDomainException(
    override val message: String = ""
) : DomainException(message) {
}
