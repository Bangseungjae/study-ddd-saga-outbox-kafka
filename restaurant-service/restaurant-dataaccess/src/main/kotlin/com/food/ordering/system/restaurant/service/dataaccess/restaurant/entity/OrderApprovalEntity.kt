package com.food.ordering.system.restaurant.service.dataaccess.restaurant.entity

import com.food.ordering.system.domain.valueobject.OrderApprovalStatus
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Table(name = "order_approval", schema = "restaurant")
@Entity
class OrderApprovalEntity(
    @Id
    val id: UUID,
    val restaurantId: UUID,
    val orderId: UUID,
    @Enumerated(EnumType.STRING)
    val status: OrderApprovalStatus,
) {
}
