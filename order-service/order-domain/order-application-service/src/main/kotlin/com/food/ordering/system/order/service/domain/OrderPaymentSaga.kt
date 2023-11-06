package com.food.ordering.system.order.service.domain

import com.food.ordering.system.domain.event.EmptyEvent
import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse
import com.food.ordering.system.order.service.domain.entity.Order
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent
import com.food.ordering.system.saga.SagaStep
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class OrderPaymentSaga(
    private val orderDomainService: OrderDomainService,
    private val orderSagaHelper: OrderSagaHelper,
) : SagaStep<PaymentResponse, OrderPaidEvent, EmptyEvent> {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun process(data: PaymentResponse): OrderPaidEvent {
        logger.info("Completing payment for order with id: ${data.orderId}")
        val order: Order = orderSagaHelper.findOrder(data.orderId)
        val domainEvent = orderDomainService.payOrder(order)
        orderSagaHelper.saveOrder(order)
        logger.info("Order with id: ${order.id.value}")
        return domainEvent
    }


    @Transactional
    override fun rollback(data: PaymentResponse): EmptyEvent {
        logger.info("Cancelling order with id: ${data.orderId}")
        val order = orderSagaHelper.findOrder(data.orderId)
        orderDomainService.cancelOrder(
            order = order,
            failureMessage = order.failureMessages
        )
        orderSagaHelper.saveOrder(order)
        logger.info("Order with id: ${order.id.value} is cancelled")
        return EmptyEvent.INSTANCE
    }

}
