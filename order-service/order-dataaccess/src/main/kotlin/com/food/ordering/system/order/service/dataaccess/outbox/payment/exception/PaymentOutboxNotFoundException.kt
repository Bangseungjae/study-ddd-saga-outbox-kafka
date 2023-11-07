package com.food.ordering.system.order.service.dataaccess.outbox.payment.exception

class PaymentOutboxNotFoundException(
    message: String? = null,
    cause: Throwable? = null,
) : RuntimeException(
    message = message,
    cause = cause
)

