package com.food.ordering.system.order.service.domain

import com.food.ordering.system.domain.event.EmptyEvent
import com.food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse
import com.food.ordering.system.order.service.domain.entity.Order
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent
import com.food.ordering.system.saga.SagaStep
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class OrderApprovalSaga(
    private val orderDomainService: OrderDomainService,
    private val orderSagaHelper: OrderSagaHelper,
) : SagaStep<RestaurantApprovalResponse, EmptyEvent, OrderCancelledEvent> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun process(restaurantApprovalResponse: RestaurantApprovalResponse): EmptyEvent {
        logger.info("Approving order with id: ${restaurantApprovalResponse.orderId}")
        val order: Order = orderSagaHelper.findOrder(restaurantApprovalResponse.orderId)
        orderDomainService.approveOrder(order)
        orderSagaHelper.saveOrder(order)
        logger.info("Order with id: ${order.id.value} is approved")
        return EmptyEvent.INSTANCE
    }

    @Transactional
    override fun rollback(restaurantApprovalResponse: RestaurantApprovalResponse): OrderCancelledEvent {
        logger.info("Cancelling order with id: ${restaurantApprovalResponse.orderId}")
        val order = orderSagaHelper.findOrder(restaurantApprovalResponse.orderId)
        val domainEvent = orderDomainService.cancelOrderPayment(
            order = order,
            failureMessage = restaurantApprovalResponse.failureMessages,
        )
        orderSagaHelper.saveOrder(order)
        logger.info("Order with id: ${order.id.value} is cancelling")
        return domainEvent
    }


}
