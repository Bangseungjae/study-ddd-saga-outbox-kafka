package com.food.ordering.system.restaurant.service.domain.mapper

import com.food.ordering.system.domain.valueobject.Money
import com.food.ordering.system.domain.valueobject.OrderId
import com.food.ordering.system.domain.valueobject.OrderStatus
import com.food.ordering.system.domain.valueobject.RestaurantId
import com.food.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest
import com.food.ordering.system.restaurant.service.domain.entity.OrderDetail
import com.food.ordering.system.restaurant.service.domain.entity.Product
import com.food.ordering.system.restaurant.service.domain.entity.Restaurant
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovalEvent
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderEventPayload
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class RestaurantDataMapper {
    fun restaurantApprovalRequestToRestaurant(restaurantApprovalRequest: RestaurantApprovalRequest): Restaurant {
        return Restaurant(
            id = RestaurantId(UUID.fromString(restaurantApprovalRequest.restaurantId)),
            active = true,
            orderDetail = OrderDetail(
                id = OrderId(UUID.fromString(restaurantApprovalRequest.orderId)),
                products = restaurantApprovalRequest.projects.map { product ->
                    Product(
                        id = product.id,
                        quantity = product.quantity,
                        available = product.available,
                    )
                },
                orderStatus = OrderStatus.valueOf(restaurantApprovalRequest.restaurantOrderStatus.name),
                totalAmount = Money(restaurantApprovalRequest.price)
            ),
        )
    }

    fun orderApprovalEventToEventPayload(orderApprovalEvent: OrderApprovalEvent): OrderEventPayload {
        return OrderEventPayload(
            orderId = orderApprovalEvent.orderApproval.orderId.value.toString(),
            restaurantId = orderApprovalEvent.restaurantId.value.toString(),
            createdAt = orderApprovalEvent.createdAt,
            orderApprovalStatus = orderApprovalEvent.orderApproval.orderApprovalStatus.name,
            failureMessages = orderApprovalEvent.failureMessages,
        )
    }
}
