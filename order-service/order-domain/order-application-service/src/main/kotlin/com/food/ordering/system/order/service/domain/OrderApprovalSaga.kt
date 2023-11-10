package com.food.ordering.system.order.service.domain

import com.food.ordering.system.domain.KOREA_DATE_TIME
import com.food.ordering.system.domain.valueobject.OrderStatus
import com.food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse
import com.food.ordering.system.order.service.domain.entity.Order
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent
import com.food.ordering.system.order.service.domain.exception.OrderDomainException
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage
import com.food.ordering.system.order.service.domain.outbox.scheduler.approval.ApprovalOutboxHelper
import com.food.ordering.system.order.service.domain.outbox.scheduler.payment.PaymentOutboxHelper
import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.saga.SagaStatus
import com.food.ordering.system.saga.SagaStatus.*
import com.food.ordering.system.saga.SagaStep
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

@Component
class OrderApprovalSaga(
    private val orderDomainService: OrderDomainService,
    private val orderSagaHelper: OrderSagaHelper,
    private val approvalOutboxHelper: ApprovalOutboxHelper,
    private val orderDataMapper: OrderDataMapper,
    private val paymentOutboxHelper: PaymentOutboxHelper,
) : SagaStep<RestaurantApprovalResponse> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun process(restaurantApprovalResponse: RestaurantApprovalResponse) {

        logger.info("restaurantApprovalResponse: saga id: ${restaurantApprovalResponse.sagaId}")

        val approvalOutboxMessage =
            approvalOutboxHelper.getApprovalOutboxMessageBySagaIdAndSagaStatus(
                sagaId = UUID.fromString(restaurantApprovalResponse.sagaId),
                sagaStatus = arrayOf(PROCESSING),
            ) ?: run {
                logger.info("An outbox message with saga id: ${restaurantApprovalResponse.sagaId} is already processed")
                return
            }


        val order: Order = approveOrder(restaurantApprovalResponse)
        val sagaStatus = orderSagaHelper.orderStatusToSagaStatus(order.orderStatus)

        approvalOutboxHelper.save(
            getUpdatedApprovalOutboxMessage(
                approvalOutboxMessage = approvalOutboxMessage,
                orderStatus = order.orderStatus,
                sagaStatus = sagaStatus
            )
        )
        paymentOutboxHelper.save(
            getUpdatedPaymentOutboxMessage(
                restaurantApprovalResponse.sagaId,
                order.orderStatus,
                sagaStatus
            )
        )


        logger.info("Order with id: ${order.id.value} is approved")
    }

    @Transactional
    override fun rollback(restaurantApprovalResponse: RestaurantApprovalResponse) {

        val orderApprovalOutboxMessage =
            approvalOutboxHelper.getApprovalOutboxMessageBySagaIdAndSagaStatus(
                UUID.fromString(restaurantApprovalResponse.sagaId),
                sagaStatus = arrayOf(PROCESSING)
            ) ?: run {
                logger.info("An outbox message with saga id: ${restaurantApprovalResponse.sagaId} is already roll back!")
                return
            }

        val domainEvent: OrderCancelledEvent = rollbackOrder(restaurantApprovalResponse)

        val sagaStatus = orderSagaHelper.orderStatusToSagaStatus(domainEvent.order.orderStatus)

        approvalOutboxHelper.save(
            getUpdatedApprovalOutboxMessage(
                approvalOutboxMessage = orderApprovalOutboxMessage,
                orderStatus = domainEvent.order.orderStatus,
                sagaStatus = sagaStatus,
            )
        )

        paymentOutboxHelper.savePaymentOutboxMessage(
            paymentEventPayload = orderDataMapper.orderCancelledEventToOrderPaymentEventPayload(domainEvent),
            orderStatus = domainEvent.order.orderStatus,
            sagaStatus = sagaStatus,
            outboxStatus = OutboxStatus.STARTED,
            sagaId = UUID.fromString(restaurantApprovalResponse.sagaId),
        )


        logger.info("Order with id: ${domainEvent.order.id.value} is cancelling")
    }



    private fun getUpdatedPaymentOutboxMessage(
        sagaId: String,
        orderStatus: OrderStatus,
        sagaStatus: SagaStatus,
    ): OrderPaymentOutboxMessage {
        val orderPaymentOutboxMessage = paymentOutboxHelper.getPaymentOutboxMessageBySagaIdAndSagaStatus(
            sagaId = UUID.fromString(sagaId),
            sagaStatus = arrayOf(PROCESSING),
        ) ?: run {
            throw OrderDomainException("Payment outbox message cannot be found! in ${PROCESSING.name} state")
        }
        orderPaymentOutboxMessage.processedAt = ZonedDateTime.now(ZoneId.of(KOREA_DATE_TIME))
        orderPaymentOutboxMessage.orderStatus = orderStatus
        orderPaymentOutboxMessage.sagaStatus = sagaStatus
        return orderPaymentOutboxMessage
    }

    private fun getUpdatedApprovalOutboxMessage(
        approvalOutboxMessage: OrderApprovalOutboxMessage,
        orderStatus: OrderStatus,
        sagaStatus: SagaStatus,
    ): OrderApprovalOutboxMessage {
        approvalOutboxMessage.processedAt = ZonedDateTime.now(ZoneId.of(KOREA_DATE_TIME))
        approvalOutboxMessage.orderStatus = orderStatus
        approvalOutboxMessage.sagaStatus = sagaStatus
        return approvalOutboxMessage
    }

    private fun approveOrder(restaurantApprovalResponse: RestaurantApprovalResponse): Order {
        logger.info("Approving order with id: ${restaurantApprovalResponse.orderId}")
        val order: Order = orderSagaHelper.findOrder(restaurantApprovalResponse.orderId)
        orderDomainService.approveOrder(order)
        orderSagaHelper.saveOrder(order)
        return order
    }

    private fun rollbackOrder(restaurantApprovalResponse: RestaurantApprovalResponse): OrderCancelledEvent {
        logger.info("Cancelling order with id: ${restaurantApprovalResponse.orderId}")
        val order = orderSagaHelper.findOrder(restaurantApprovalResponse.orderId)
        val domainEvent = orderDomainService.cancelOrderPayment(
            order = order,
            failureMessage = restaurantApprovalResponse.failureMessages,
        )
        orderSagaHelper.saveOrder(order)
        return domainEvent
    }
}
