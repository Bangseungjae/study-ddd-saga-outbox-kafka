package com.food.ordering.system.restaurant.service.domain

import com.food.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest
import com.food.ordering.system.restaurant.service.domain.ports.input.message.listener.RestaurantApprovalRequestMessageListener
import org.springframework.stereotype.Service

@Service
class RestaurantApprovalRequestMessageListenerImpl(
    private val restaurantApprovalRequestHelper: RestaurantApprovalRequestHelper,
) : RestaurantApprovalRequestMessageListener {

    override fun approveOrder(restaurantApprovalRequest: RestaurantApprovalRequest) {
        restaurantApprovalRequestHelper.persistOrderApproval(restaurantApprovalRequest)
    }
}
