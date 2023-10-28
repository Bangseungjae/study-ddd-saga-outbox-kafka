package com.food.ordering.system.order.service.dataaccess.order.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.util.UUID

@IdClass(OrderItemEntityId::class)
@Table(name = "order_items")
@Entity
class OrderItemEntity(
    @Id
    val id: UUID,

    @Id
    @JoinColumn(name = "ORDER_ID")


    val productId: UUID,
    val price: BigDecimal,
    val quantity: Int,
    val subTotal: BigDecimal,
) {
    @ManyToOne(cascade = [CascadeType.ALL])
    lateinit var order: OrderEntity

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OrderItemEntity) return false

        if (price != other.price) return false
        if (quantity != other.quantity) return false
        if (subTotal != other.subTotal) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + order.hashCode()
        result = 31 * result + productId.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + quantity
        result = 31 * result + subTotal.hashCode()
        return result
    }
}
