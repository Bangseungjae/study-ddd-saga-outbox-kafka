package com.food.ordering.system.order.service.domain.exception

import com.food.ordering.system.domain.exception.DomainException

class OrderDomainException : DomainException {
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}
