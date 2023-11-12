package com.food.ordering.system.order.service.domain.entity

import com.food.ordering.system.domain.entity.AggregateRoot
import com.food.ordering.system.domain.valueobject.CustomerId
import java.util.UUID

class Customer(
    customerId: CustomerId,
    val username: String,
    val firstName: String,
    val lastName: String,
) : AggregateRoot<CustomerId>(customerId) {
}
