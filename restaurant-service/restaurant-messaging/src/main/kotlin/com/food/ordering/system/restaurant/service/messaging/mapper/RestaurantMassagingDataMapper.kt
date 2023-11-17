package com.food.ordering.system.restaurant.service.messaging.mapper

import com.food.ordering.system.domain.event.payload.OrderApprovalEventPayload
import com.food.ordering.system.domain.valueobject.ProductId
import com.food.ordering.system.domain.valueobject.RestaurantOrderStatus
import com.food.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest
import com.food.ordering.system.restaurant.service.domain.entity.Product
import debezium.order.restaurant_approval_outbox.Value
import java.time.Instant
import java.util.UUID


fun OrderApprovalEventPayload.toRestaurantApproval(
    restaurantApprovalRequestAvroModel: Value,
): RestaurantApprovalRequest = run {
    RestaurantApprovalRequest(
        id = restaurantApprovalRequestAvroModel.id,
        sagaId = restaurantApprovalRequestAvroModel.sagaId,
        restaurantId = restaurantId,
        orderId = orderId,
        restaurantOrderStatus = RestaurantOrderStatus.valueOf(restaurantOrderStatus),
        projects = products.map {
            Product(
                id = ProductId(UUID.fromString(it.id)),
                quantity = it.quantity,
                available = true,
            )
        },
        price = price,
        createdAt = Instant.parse(restaurantApprovalRequestAvroModel.createdAt)
    )
}
