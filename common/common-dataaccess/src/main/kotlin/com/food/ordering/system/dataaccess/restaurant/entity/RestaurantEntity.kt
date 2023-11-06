package com.food.ordering.system.dataaccess.restaurant.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import java.math.BigDecimal
import java.util.UUID

@IdClass(RestaurantEntityId::class)
@Table(name = "order_restaurant_m_view", schema = "restaurant")
@Entity
class RestaurantEntity(
    @Id
    var restaurantId: UUID,
    @Id
    var productId: UUID,

    val restaurantName: String,
    val restaurantActive: Boolean,
    val productName: String,
    val productPrice: BigDecimal,
    val productAvailable: Boolean,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RestaurantEntity) return false

        if (restaurantId != other.restaurantId) return false
        if (productId != other.productId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = restaurantId.hashCode()
        result = 31 * result + productId.hashCode()
        return result
    }
}
