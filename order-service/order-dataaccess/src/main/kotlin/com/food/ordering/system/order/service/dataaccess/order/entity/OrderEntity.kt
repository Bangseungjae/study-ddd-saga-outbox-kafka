package com.food.ordering.system.order.service.dataaccess.order.entity

import com.food.ordering.system.domain.valueobject.OrderStatus
import jakarta.persistence.*
import java.math.BigDecimal
import java.util.UUID

@Table(name = "orders")
@Entity
class OrderEntity(
    @Id
    val id: UUID,
    val customerId: UUID,
    val restaurantId: UUID,
    val trackingId: UUID,
    val price: BigDecimal,

    @Enumerated(EnumType.STRING)
    val orderStatus: OrderStatus,
    val failureMessages: String,

    @OneToOne(mappedBy = "order", cascade = [CascadeType.ALL])
    val address: OrderAddressEntity,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL])
    val items: List<OrderItemEntity> = mutableListOf()
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OrderEntity) return false

        if (id != other.id) return false
        if (price != other.price) return false
        if (orderStatus != other.orderStatus) return false
        if (failureMessages != other.failureMessages) return false
        if (address != other.address) return false
        if (items != other.items) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + orderStatus.hashCode()
        result = 31 * result + failureMessages.hashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + items.hashCode()
        return result
    }
}
