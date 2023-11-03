package com.food.ordering.system.restaurant.service.domain.entity

import com.food.ordering.system.domain.entity.BaseEntity
import com.food.ordering.system.domain.valueobject.Money
import com.food.ordering.system.domain.valueobject.ProductId

class Product(
    id: ProductId,
    var name: String = "",
    var price: Money = Money.ZERO,
    val quantity: Int,
    var available: Boolean = true,
) : BaseEntity<ProductId>(id) {

    fun updateWithConfirmedNamePriceAndAvailability(
        name: String,
        price: Money,
        available: Boolean,
    ) {
        this.name = name
        this.price = price
        this.available = available
    }

}
