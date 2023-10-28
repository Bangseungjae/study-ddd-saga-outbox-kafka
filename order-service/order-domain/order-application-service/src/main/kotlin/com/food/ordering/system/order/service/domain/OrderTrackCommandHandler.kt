package com.food.ordering.system.order.service.domain

import com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse
import com.food.ordering.system.order.service.domain.entity.Order
import com.food.ordering.system.order.service.domain.exception.OrderDomainException
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository
import com.food.ordering.system.order.service.domain.valueobject.TrackingId
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class OrderTrackCommandHandler(
    private val orderDataMapper: OrderDataMapper,
    private val orderRepository: OrderRepository,
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun trackOrder(trackOrderQuery: TrackOrderQuery): TrackOrderResponse {
        val orderResult: Order = orderRepository.findByTrackingId(TrackingId(trackOrderQuery.orderTrackingId))
            ?: run {
                logger.info("Could not find order with tracking id: ${trackOrderQuery.orderTrackingId}")
                throw OrderDomainException("Could not find order with tracking id: ${trackOrderQuery.orderTrackingId}")
            }
        return orderDataMapper.orderToTrackOrderResponse(orderResult)
    }
}
