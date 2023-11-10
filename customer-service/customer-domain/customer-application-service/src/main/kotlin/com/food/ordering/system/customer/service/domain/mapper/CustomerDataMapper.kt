package com.food.ordering.system.customer.service.domain.mapper

import com.food.ordering.system.customer.service.domain.create.CreateCustomerCommand
import com.food.ordering.system.customer.service.domain.create.CreateCustomerResponse
import com.food.ordering.system.customer.service.domain.entity.Customer
import com.food.ordering.system.domain.valueobject.CustomerId

fun CreateCustomerCommand.toCustomer() = run {
    Customer(
        customerId = CustomerId(customerId),
        username = username,
        firstName = firstName,
        lastName = lastName,
    )
}

fun Customer.toCreateCustomerCommand(message: String): CreateCustomerResponse = run {
    CreateCustomerResponse(
        customerId = id.value,
        message = message,
    )
}
