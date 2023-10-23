package com.food.ordering.system.order.service.domain.entity

import com.food.ordering.system.domain.entity.BaseEntity
import com.food.ordering.system.domain.valueobject.Money
import com.food.ordering.system.domain.valueobject.OrderId
import com.food.ordering.system.order.service.domain.valueobject.OrderItemId

class OrderItem(
    orderItemId: OrderItemId,
    val orderId: OrderId,
    val product: Product,
    val quantity: Int,
    val price: Money,
    val subTotal: Money,
) : BaseEntity<OrderItemId>(orderItemId){

    fun isPriceValid(): Boolean{
        return price.isGreaterThanZero() &&
                price == product.price &&
                price.multiply(quantity) == subTotal
    }
}
