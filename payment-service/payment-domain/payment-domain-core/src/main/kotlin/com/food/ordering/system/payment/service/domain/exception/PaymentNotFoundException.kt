package com.food.ordering.system.payment.service.domain.exception

import com.food.ordering.system.domain.exception.DomainException

class PaymentNotFoundException(
    message: String = ""
) : DomainException(message) {
}
