package com.food.ordering.system.order.service.dataaccess.order.entity

import java.io.Serializable
import java.util.UUID


class OrderItemEntityId(
    val id: UUID,
    val order: OrderEntity,
) : Serializable{

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OrderItemEntityId) return false

        if (id != other.id) return false
        if (order != other.order) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + order.hashCode()
        return result
    }
}
