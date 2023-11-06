package com.food.ordering.system.restaurant.service.messaging.mapper

import com.food.ordering.system.domain.valueobject.ProductId
import com.food.ordering.system.kafka.order.avro.model.OrderApprovalStatus
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel
import com.food.ordering.system.kafka.order.avro.model.RestaurantOrderStatus
import com.food.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest
import com.food.ordering.system.restaurant.service.domain.entity.Product
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovalEvent
import com.food.ordering.system.restaurant.service.domain.event.OrderRejectedEvent
import java.util.UUID

fun OrderApprovalEvent.toRestaurantApprovalResponseAvroModel(): RestaurantApprovalResponseAvroModel = run {
    RestaurantApprovalResponseAvroModel.newBuilder()
        .setId(UUID.randomUUID())
        .setSagaId(UUID.randomUUID())
        .setOrderId(orderApproval.orderId.value)
        .setRestaurantId(orderApproval.restaurantId.value)
        .setCreatedAt(createdAt.toInstant())
        .setOrderApprovalStatus(OrderApprovalStatus.valueOf(orderApproval.orderApprovalStatus.name))
        .setFailureMessages(failureMessages)
        .build()
}

fun OrderRejectedEvent.toRestaurantApprovalResponseAvroModel(): RestaurantApprovalResponseAvroModel = run {
    RestaurantApprovalResponseAvroModel.newBuilder()
        .setId(UUID.randomUUID())
        .setSagaId(UUID.randomUUID())
        .setOrderId(orderApproval.orderId.value)
        .setRestaurantId(orderApproval.restaurantId.value)
        .setCreatedAt(createdAt.toInstant())
        .setOrderApprovalStatus(OrderApprovalStatus.valueOf(orderApproval.orderApprovalStatus.name))
        .setFailureMessages(failureMessages)
        .build()
}

fun RestaurantApprovalRequestAvroModel.toRestaurantApproval(): RestaurantApprovalRequest = run {
    RestaurantApprovalRequest(
        id = id.toString(),
        sagaId = sagaId.toString(),
        restaurantId = restaurantId.toString(),
        orderId = orderId.toString(),
        restaurantOrderStatus = com.food.ordering.system.domain.valueobject.RestaurantOrderStatus.valueOf(
            restaurantOrderStatus.name
        ),
        projects = products.map { avroModel ->
            Product(
                id = ProductId(UUID.fromString(avroModel.id)),
                quantity = avroModel.quantity,
                available = true,
            )
        },
        price = price,
        createdAt = createdAt
    )
}
