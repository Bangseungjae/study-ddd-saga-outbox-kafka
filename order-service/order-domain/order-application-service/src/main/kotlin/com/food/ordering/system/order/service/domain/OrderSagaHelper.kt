package com.food.ordering.system.order.service.domain

import com.food.ordering.system.domain.valueobject.OrderId
import com.food.ordering.system.order.service.domain.entity.Order
import com.food.ordering.system.order.service.domain.exception.OrderNotFoundException
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
class OrderSagaHelper(
    private val orderRepository: OrderRepository,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun findOrder(orderId: String): Order = orderRepository.findById(OrderId(UUID.fromString(orderId)))
        ?: run {
            logger.error("Order with id: $orderId could not be found!")
            throw OrderNotFoundException("Order with id: $orderId could not be found!")
        }

    fun saveOrder(order: Order) {
        orderRepository.save(order)
    }
}
