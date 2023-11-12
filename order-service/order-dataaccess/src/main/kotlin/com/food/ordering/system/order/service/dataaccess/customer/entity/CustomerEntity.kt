package com.food.ordering.system.order.service.dataaccess.customer.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Table(name = "customers")
@Entity
class CustomerEntity(
    @Id
    val id: UUID,

    val username: String,
    val firstName: String,
    val lastName: String,
) {
}
