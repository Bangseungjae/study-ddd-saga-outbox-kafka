package com.food.ordering.system.order.service.dataaccess.order.mapper

import com.food.ordering.system.domain.valueobject.*
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderAddressEntity
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderEntity
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderItemEntity
import com.food.ordering.system.order.service.domain.entity.Order
import com.food.ordering.system.order.service.domain.entity.OrderItem
import com.food.ordering.system.order.service.domain.entity.Product
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper
import com.food.ordering.system.order.service.domain.valueobject.OrderItemId
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress
import com.food.ordering.system.order.service.domain.valueobject.TrackingId
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import kotlin.collections.List

const val FAILURE_MESSAGE_DELIMITER = ","

@Component
class OrderDataAccessMapper(
    private val orderDataMapper: OrderDataMapper,
) {

    fun orderToOrderEntity(order: Order): OrderEntity {
        val orderEntity = OrderEntity(
            id = order.id.value,
            customerId = order.customerId.value,
            restaurantId = order.restaurantId.value,
            trackingId = order.trackingId.value,
            address = deliveryAddressToAddressEntity(order.deliveryAddress),
            price = order.price.amount,
            failureMessages = order.failureMessages.joinToString(separator = ","),
            items = orderItemsToOrderItemEntities(order.items),
            orderStatus = order.orderStatus,
        )
        orderEntity.address.order = orderEntity
        orderEntity.items.forEach { orderItemEntity -> orderItemEntity.order = orderEntity }
        return orderEntity
    }


    private fun orderItemsToOrderItemEntities(items: List<OrderItem>): List<OrderItemEntity> = items.map { orderItem ->
        OrderItemEntity(
            id = orderItem.id.value,
            productId = orderItem.product.id.value,
            price = orderItem.price.amount,
            quantity = orderItem.quantity,
            subTotal = orderItem.subTotal.amount
        )
    }


    private fun deliveryAddressToAddressEntity(deliveryAddress: StreetAddress): OrderAddressEntity = OrderAddressEntity(
        deliveryAddress.id,
        street = deliveryAddress.street,
        postalCode = deliveryAddress.postalCode,
        city = deliveryAddress.city
    )
}

fun OrderEntity.orderEntityToOrder(): Order = let {
    Order(
        orderId = OrderId(it.id),
        customerId = CustomerId(it.customerId),
        restaurantId = RestaurantId(it.restaurantId),
        deliveryAddress = it.address.addressEntityToDeliveryAddress(),
        price = Money(amount = it.price),
        items = it.items.orderItemEntitiesToOrderItems(),
        trackingId = TrackingId(it.trackingId),
        failureMessages = if(it.failureMessages.isEmpty()) mutableListOf() else
            it.failureMessages.split(FAILURE_MESSAGE_DELIMITER).toMutableList(),
        orderStatus = orderStatus
    )
}
private fun OrderAddressEntity.addressEntityToDeliveryAddress(): StreetAddress = let {
    StreetAddress(
        id = it.id,
        street = it.street,
        postalCode = it.postalCode,
        city = it.city,
    )
}
private fun List<OrderItemEntity>.orderItemEntitiesToOrderItems(): List<OrderItem> = let {
    it.map { orderItemEntity ->
        OrderItem(
            orderItemId = OrderItemId(orderItemEntity.id),
            orderId = OrderId(orderItemEntity.order.id),
            price = Money(orderItemEntity.price),
            product = Product(ProductId(orderItemEntity.productId)),
            quantity = orderItemEntity.quantity,
            subTotal = Money(orderItemEntity.subTotal),
        )
    }
}
