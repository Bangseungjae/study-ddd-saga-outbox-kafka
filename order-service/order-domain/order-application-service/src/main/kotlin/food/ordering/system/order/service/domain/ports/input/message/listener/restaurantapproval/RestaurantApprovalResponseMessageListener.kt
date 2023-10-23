package food.ordering.system.order.service.domain.ports.input.message.listener.restaurantapproval

import food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse

interface RestaurantApprovalResponseMessageListener {

    fun orderApproved(restaurantApprovalResponse: RestaurantApprovalResponse)

    fun orderRejected(restaurantApprovalResponse: RestaurantApprovalResponse)
}
