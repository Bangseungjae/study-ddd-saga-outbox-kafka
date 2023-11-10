package com.food.ordering.system.customer.service.domain.create

import java.util.UUID

data class CreateCustomerResponse(
    val customerId: UUID,
    val message: String,
)
