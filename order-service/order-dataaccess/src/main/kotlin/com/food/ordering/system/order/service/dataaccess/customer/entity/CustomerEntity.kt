package com.food.ordering.system.order.service.dataaccess.customer.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Table(name = "order_customer_m_view", schema = "customer")
@Entity
class CustomerEntity(
    @Id
    val id: UUID,
) {
}
