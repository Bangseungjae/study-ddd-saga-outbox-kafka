package com.food.ordering.system.order.service.domain.dto.create

import jakarta.validation.constraints.Max

data class OrderAddress(
    @Max(value = 50)
    val street: String,
    @Max(value = 10)
    val postalCode: String,
    @Max(value = 50)
    val city: String,
)
