package com.food.ordering.system.restaurant.service.domain.entity

import com.food.ordering.system.domain.entity.AggregateRoot
import com.food.ordering.system.domain.valueobject.Money
import com.food.ordering.system.domain.valueobject.OrderApprovalStatus
import com.food.ordering.system.domain.valueobject.OrderStatus
import com.food.ordering.system.domain.valueobject.RestaurantId
import com.food.ordering.system.restaurant.service.domain.valueobject.OrderApprovalId
import java.util.*

class Restaurant private constructor(
    value: RestaurantId,

    var active: Boolean,
    val orderDetail: OrderDetail,
    var orderApproval: OrderApproval
) : AggregateRoot<RestaurantId>(value) {

    fun validateOrder(failureMessages: MutableList<String>) {
        if (orderDetail.orderStatus != OrderStatus.PAID) {
            failureMessages.add("Payment is not completed for order: ${orderDetail.id}")
        }
        val totalAmount = orderDetail.products.map { product ->
            if (!product.available) {
                failureMessages.add("Product with id: ${product.id.value} is not available")
            }
            product.price.multiply(product.quantity)
        }.fold(Money.ZERO) { total, money -> total.add(money) }

        if (totalAmount != orderDetail.totalAmount) {
            failureMessages.add("Price total is not correct for order: ${orderDetail.id}")
        }
    }

    fun constructOrderApproval(orderApprovalStatus: OrderApprovalStatus) {
        this.orderApproval.orderApprovalStatus = orderApprovalStatus
    }

    companion object {
        operator fun invoke(
            id: RestaurantId,
            orderDetail: OrderDetail,
            active: Boolean,
            orderApprovalStatus: OrderApprovalStatus = OrderApprovalStatus.APPROVED,
        ): Restaurant {
            return Restaurant(
                value = id,
                active = active,
                orderDetail = orderDetail,
                orderApproval = OrderApproval(
                    id = OrderApprovalId(UUID.randomUUID()),
                    restaurantId = id,
                    orderId = orderDetail.id,
                    orderApprovalStatus = orderApprovalStatus
                ),
            )
        }
    }
}

