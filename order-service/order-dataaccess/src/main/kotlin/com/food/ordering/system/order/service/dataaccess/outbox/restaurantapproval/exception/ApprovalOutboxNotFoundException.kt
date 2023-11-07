package com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.exception

class ApprovalOutboxNotFoundException(
    message: String? = null,
    cause: Throwable? = null,
) : RuntimeException(
    message = message,
    cause = cause
)
