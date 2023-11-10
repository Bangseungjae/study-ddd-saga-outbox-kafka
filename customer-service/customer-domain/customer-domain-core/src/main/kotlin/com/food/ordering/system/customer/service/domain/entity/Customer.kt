package com.food.ordering.system.customer.service.domain.entity

import com.food.ordering.system.domain.entity.AggregateRoot
import com.food.ordering.system.domain.valueobject.CustomerId

class Customer(
    customerId: CustomerId,
    val username: String,
    val firstName: String,
    val lastName: String,
) : AggregateRoot<CustomerId>(customerId) {
}
