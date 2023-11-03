package com.food.ordering.system.restaurant.service.dataaccess.restaurant.mapper

import com.food.ordering.system.dataaccess.restaurant.entity.RestaurantEntity
import com.food.ordering.system.domain.valueobject.*
import com.food.ordering.system.restaurant.service.dataaccess.restaurant.entity.OrderApprovalEntity
import com.food.ordering.system.restaurant.service.domain.entity.OrderApproval
import com.food.ordering.system.restaurant.service.domain.entity.OrderDetail
import com.food.ordering.system.restaurant.service.domain.entity.Product
import com.food.ordering.system.restaurant.service.domain.entity.Restaurant
import com.food.ordering.system.restaurant.service.domain.valueobject.OrderApprovalId
import java.util.UUID

fun Restaurant.toRestaurantProducts(): List<UUID> = let {
    it.orderDetail.products.map { product: Product ->
        product.id.value
    }.toList()
}

fun List<RestaurantEntity>.toRestaurant(): Restaurant = let {
    val restaurantEntity = it.first()

    val restaurantProducts = it.map { entity ->
        Product(
            id = ProductId(entity.productId),
            name = entity.productName,
            price = Money(entity.productPrice),
            quantity = 1,
            available = entity.productAvailable
        )
    }

    Restaurant(
        id = RestaurantId(restaurantEntity.restaurantId),
        orderDetail = OrderDetail(
            id = OrderId(UUID.randomUUID()),
            orderStatus = OrderStatus.PENDING,
            totalAmount = Money(restaurantEntity.productPrice),
            products = restaurantProducts,
        ),
        active = restaurantEntity.restaurantActive,
    )
}

fun OrderApproval.toOrderApprovalEntity(): OrderApprovalEntity  = run {
    OrderApprovalEntity(
        id = id.value,
        restaurantId = restaurantId.value,
        orderId = orderId.value,
        status = orderApprovalStatus,
    )
}

fun OrderApprovalEntity.toOrderApproval(): OrderApproval  = run {
    OrderApproval(
        id = OrderApprovalId(orderId),
        restaurantId = RestaurantId(restaurantId),
        orderId = OrderId(orderId),
        orderApprovalStatus = status,
    )
}
