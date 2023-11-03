package com.food.ordering.system.restaurant.service.domain

import com.food.ordering.system.domain.valueobject.OrderApprovalStatus
import com.food.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest
import com.food.ordering.system.restaurant.service.domain.ports.input.message.listener.RestaurantApprovalRequestMessageListener
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.OrderApprovedMessagePublisher
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.OrderRejectedMessagePublisher
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RestaurantApprovalRequestMessageListenerImpl(
    private val restaurantApprovalRequestHelper: RestaurantApprovalRequestHelper,
    private val orderApprovedMessagePublisher: OrderApprovedMessagePublisher,
    private val orderRejectedMessagePublisher: OrderRejectedMessagePublisher,
) : RestaurantApprovalRequestMessageListener {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun approveOrder(restaurantApprovalRequest: RestaurantApprovalRequest) {
        val orderApprovalEvent = restaurantApprovalRequestHelper.persistOrderApproval(restaurantApprovalRequest)
        orderApprovalEvent.fire()
        orderApprovalEvent.orderApproval.orderApprovalStatus
    }
}
