package com.food.ordering.system.order.service.domain

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse
import com.food.ordering.system.order.service.domain.ports.input.service.OrderApplicationService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
 private class OrderApplicationServiceImpl (
    private val orderCreateCommandHandler: OrderCreateCommandHandler,
    private val orderTrackCommandHandler: OrderTrackCommandHandler,
) : OrderApplicationService {

    override fun createOrder(createOrderCommand: CreateOrderCommand): CreateOrderResponse {
        return orderCreateCommandHandler.createOrder(createOrderCommand)
    }

    override fun trackOrder(trackOrderQuery: TrackOrderQuery): TrackOrderResponse {
        return orderTrackCommandHandler.trackOrder(trackOrderQuery)
    }
}
