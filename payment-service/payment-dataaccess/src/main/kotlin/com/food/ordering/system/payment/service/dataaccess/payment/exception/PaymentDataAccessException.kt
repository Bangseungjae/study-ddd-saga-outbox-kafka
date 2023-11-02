package com.food.ordering.system.payment.service.dataaccess.payment.exception

class PaymentDataAccessException(
    override val message: String? = ""
) : RuntimeException(message)

