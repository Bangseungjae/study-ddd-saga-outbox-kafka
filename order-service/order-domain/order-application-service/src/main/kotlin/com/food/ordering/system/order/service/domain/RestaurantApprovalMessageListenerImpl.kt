package com.food.ordering.system.order.service.domain

import com.food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse
import com.food.ordering.system.order.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RestaurantApprovalMessageListenerImpl : RestaurantApprovalResponseMessageListener {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun orderApproved(restaurantApprovalResponse: RestaurantApprovalResponse) {
        TODO("Not yet implemented")
    }

    override fun orderRejected(restaurantApprovalResponse: RestaurantApprovalResponse) {
        TODO("Not yet implemented")
    }
}
