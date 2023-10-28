package com.food.ordering.system.order.service.dataaccess.order.adapter

import com.food.ordering.system.order.service.dataaccess.order.mapper.OrderDataAccessMapper
import com.food.ordering.system.order.service.dataaccess.order.mapper.orderEntityToOrder
import com.food.ordering.system.order.service.dataaccess.order.repository.OrderJpaRepository
import com.food.ordering.system.order.service.domain.entity.Order
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository
import com.food.ordering.system.order.service.domain.valueobject.TrackingId
import org.springframework.stereotype.Component

@Component
class OrderRepositoryImpl(
    private val orderJpaRepository: OrderJpaRepository,
    private val orderDataAccessMapper: OrderDataAccessMapper
) : OrderRepository {
    override fun save(order: Order): Order {
        return orderJpaRepository.save(orderDataAccessMapper.orderToOrderEntity(order))
            .orderEntityToOrder()
    }

    override fun findByTrackingId(trackingId: TrackingId): Order? {
        return orderJpaRepository.findByTrackingId(trackingId = trackingId.value)
            ?.orderEntityToOrder()
    }
}
