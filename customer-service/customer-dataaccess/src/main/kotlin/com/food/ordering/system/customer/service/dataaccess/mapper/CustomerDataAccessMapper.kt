package com.food.ordering.system.customer.service.dataaccess.mapper

import com.food.ordering.system.customer.service.dataaccess.entity.CustomerEntity
import com.food.ordering.system.customer.service.domain.entity.Customer
import com.food.ordering.system.domain.valueobject.CustomerId

fun CustomerEntity.toCustomer(): Customer = run {
    Customer(
        customerId = CustomerId(id),
        username = username,
        firstName = firstName,
        lastName = lastName,
    )
}

fun Customer.toCustomerEntity(): CustomerEntity = run {
    CustomerEntity(
        id = id.value,
        username = username,
        firstName = firstName,
        lastName = lastName,
    )
}
