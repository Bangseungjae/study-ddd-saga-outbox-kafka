package com.food.ordering.system.order.service.domain

import com.food.ordering.system.domain.KOREA_DATE_TIME
import com.food.ordering.system.domain.valueobject.OrderStatus
import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse
import com.food.ordering.system.order.service.domain.entity.Order
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage
import com.food.ordering.system.order.service.domain.outbox.scheduler.approval.ApprovalOutboxHelper
import com.food.ordering.system.order.service.domain.outbox.scheduler.payment.PaymentOutboxHelper
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository
import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.saga.SagaStatus
import com.food.ordering.system.saga.SagaStep
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

@Component
class OrderPaymentSaga(
    private val orderDomainService: OrderDomainService,
    private val orderSagaHelper: OrderSagaHelper,
    private val paymentOutboxHelper: PaymentOutboxHelper,
    private val orderRepository: OrderRepository,
    private val approvalOutboxHelper: ApprovalOutboxHelper,
    private val orderDataMapper: OrderDataMapper,
) : SagaStep<PaymentResponse> {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun process(data: PaymentResponse) {

        val orderPaymentOutboxResponse =
            paymentOutboxHelper.getPaymentOutboxMessageBySagaIdAndSagaStatus(
                sagaId = UUID.fromString(data.sagaId),
                sagaStatus = arrayOf(SagaStatus.STARTED)
            )

        if (orderPaymentOutboxResponse == null) {
            logger.info("An outbox message with saga id: ${data.sagaId} is already processed!")
            return
        }

        logger.info("Completing payment for order with id: ${data.orderId}")
        val order: Order = orderSagaHelper.findOrder(data.orderId)
        val domainEvent = orderDomainService.payOrder(order)
        orderRepository.save(order)

        val sagaStatus = orderSagaHelper.orderStatusToSagaStatus(order.orderStatus)

        paymentOutboxHelper.save(
            getUpdatedPaymentOutboxMessage(
                orderPaymentOutboxResponse = orderPaymentOutboxResponse,
                orderStatus =  domainEvent.order.orderStatus,
                sagaStatus =  sagaStatus
            )
        )
        approvalOutboxHelper.saveApprovalOutboxMessage(
            orderApprovalEventPayload = orderDataMapper.orderPaidEventToOrderApprovalEventPayload(domainEvent),
            orderStatus = domainEvent.order.orderStatus,
            outboxStatus = OutboxStatus.STARTED,
            sagaStatus = sagaStatus,
            sagaId = UUID.fromString(data.sagaId)
        )

        logger.info("Order with id: ${order.id.value} is paid")
    }


    @Transactional
    override fun rollback(data: PaymentResponse) {
        logger.info("Cancelling order with id: ${data.orderId}")
        val order = orderSagaHelper.findOrder(data.orderId)
        orderDomainService.cancelOrder(
            order = order,
            failureMessage = order.failureMessages
        )
        orderSagaHelper.saveOrder(order)
        logger.info("Order with id: ${order.id.value} is cancelled")
    }

    private fun getUpdatedPaymentOutboxMessage(
        orderPaymentOutboxResponse: OrderPaymentOutboxMessage,
        orderStatus: OrderStatus,
        sagaStatus: SagaStatus,
    ): OrderPaymentOutboxMessage {
        orderPaymentOutboxResponse.processedAt = ZonedDateTime.now(ZoneId.of(KOREA_DATE_TIME))
        orderPaymentOutboxResponse.orderStatus = orderStatus
        orderPaymentOutboxResponse.sagaStatus = sagaStatus
        return orderPaymentOutboxResponse
    }
}
