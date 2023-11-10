package com.food.ordering.system.customer.service.domain.create

import java.util.UUID

data class CreateCustomerCommand(
    val customerId: UUID,
    val username: String,
    val firstName: String,
    val lastName: String,
)
