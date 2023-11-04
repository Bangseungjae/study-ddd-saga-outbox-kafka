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
        .setId(UUID.randomUUID().toString())
        .setSagaId("")
        .setOrderId(orderApproval.orderId.value.toString())
        .setRestaurantId(orderApproval.restaurantId.value.toString())
        .setCreatedAt(createdAt.toInstant())
        .setOrderApprovalStatus(OrderApprovalStatus.valueOf(orderApproval.orderApprovalStatus.name))
        .setFailureMessages(failureMessages)
        .build()
}

fun OrderRejectedEvent.toRestaurantApprovalResponseAvroModel(): RestaurantApprovalResponseAvroModel = run {
    RestaurantApprovalResponseAvroModel.newBuilder()
        .setId(UUID.randomUUID().toString())
        .setSagaId("")
        .setOrderId(orderApproval.orderId.value.toString())
        .setRestaurantId(orderApproval.restaurantId.value.toString())
        .setCreatedAt(createdAt.toInstant())
        .setOrderApprovalStatus(OrderApprovalStatus.valueOf(orderApproval.orderApprovalStatus.name))
        .setFailureMessages(failureMessages)
        .build()
}

fun RestaurantApprovalRequestAvroModel.toRestaurantApproval(): RestaurantApprovalRequest = run {
    RestaurantApprovalRequest(
        id = getId(),
        sagaId = getSagaId(),
        restaurantId = getRestaurantId(),
        orderId = getOrderId(),
        restaurantOrderStatus = com.food.ordering.system.domain.valueobject.RestaurantOrderStatus.valueOf(getRestaurantOrderStatus().name),
        projects = getProducts().map { avroModel ->
            Product(
                id = ProductId(UUID.fromString(avroModel.getId())),
                quantity = avroModel.getQuantity()
            )
        },
        price = getPrice(),
        createdAt = getCreatedAt()
    )
}
