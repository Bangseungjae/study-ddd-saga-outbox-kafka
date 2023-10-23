package com.food.ordering.system.order.service.domain.entity

import com.food.ordering.system.domain.entity.BaseEntity
import com.food.ordering.system.domain.valueobject.Money
import com.food.ordering.system.domain.valueobject.ProductId
import java.math.BigDecimal

class Product(
    productId: ProductId,
    var name: String? = "",
    var price: Money? = Money(BigDecimal.ZERO),
) : BaseEntity<ProductId>(productId) {
    fun updateWithConfirmedNameAndPrice(name: String, price: Money) {
        this.name = name
        this.price = price
    }
}
