package com.food.ordering.system.order.service.domain

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse
import com.food.ordering.system.order.service.domain.entity.Customer
import com.food.ordering.system.order.service.domain.entity.Order
import com.food.ordering.system.order.service.domain.entity.Restaurant
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent
import com.food.ordering.system.order.service.domain.exception.OrderDomainException
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper
import com.food.ordering.system.order.service.domain.outbox.scheduler.payment.PaymentOutboxHelper
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository
import com.food.ordering.system.outbox.OutboxStatus
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Component
class OrderCreateCommandHandler(
    private val orderDataMapper: OrderDataMapper,
    private val paymentOutboxHelper: PaymentOutboxHelper,
    private val orderCreateHelper: OrderCreateHelper,
    private val orderSagaHelper: OrderSagaHelper,
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun createOrder(createOrderCommand: CreateOrderCommand): CreateOrderResponse {
        val orderCreatedEvent = orderCreateHelper.persistOrder(createOrderCommand)
        logger.info("Order is created with id: ${orderCreatedEvent.order.id.value}")
        val createOrderResponse = orderDataMapper.orderToCreateOrderResponse(
            order = orderCreatedEvent.order,
            message = "Order created successfully"
        )
        paymentOutboxHelper.savePaymentOutboxMessage(
            sagaId = UUID.randomUUID(),
            paymentEventPayload = orderDataMapper.orderCreatedEventToOrderPaymentEventPayload(orderCreatedEvent),
            sagaStatus = orderSagaHelper.orderStatusToSagaStatus(orderCreatedEvent.order.orderStatus),
            orderStatus = orderCreatedEvent.order.orderStatus,
            outboxStatus = OutboxStatus.STARTED,
        )

        logger.info("Returning CreateOrderResponse with order id: ${orderCreatedEvent.order.id}")
        return createOrderResponse
    }
}
