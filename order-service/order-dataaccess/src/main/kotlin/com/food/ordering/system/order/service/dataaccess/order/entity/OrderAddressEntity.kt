package com.food.ordering.system.order.service.dataaccess.order.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.util.UUID

@Table(name = "order_address")
@Entity
class OrderAddressEntity(
    @Id
    val id: UUID,

    val street: String,
    val postalCode: String,
    val city: String,
) {
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "ORDER_ID")
    lateinit var order: OrderEntity

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OrderAddressEntity) return false

        if (id != other.id) return false
        if (street != other.street) return false
        if (postalCode != other.postalCode) return false
        if (city != other.city) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + street.hashCode()
        result = 31 * result + postalCode.hashCode()
        result = 31 * result + city.hashCode()
        return result
    }
}
