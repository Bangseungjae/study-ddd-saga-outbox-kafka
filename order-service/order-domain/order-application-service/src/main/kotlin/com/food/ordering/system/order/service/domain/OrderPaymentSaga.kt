package com.food.ordering.system.order.service.domain

import com.food.ordering.system.domain.KOREA_DATE_TIME
import com.food.ordering.system.domain.valueobject.OrderId
import com.food.ordering.system.domain.valueobject.OrderStatus
import com.food.ordering.system.domain.valueobject.PaymentStatus
import com.food.ordering.system.domain.valueobject.PaymentStatus.*
import com.food.ordering.system.domain.valueobject.PaymentStatus.FAILED
import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse
import com.food.ordering.system.order.service.domain.entity.Order
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent
import com.food.ordering.system.order.service.domain.exception.OrderDomainException
import com.food.ordering.system.order.service.domain.exception.OrderNotFoundException
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage
import com.food.ordering.system.order.service.domain.outbox.scheduler.approval.ApprovalOutboxHelper
import com.food.ordering.system.order.service.domain.outbox.scheduler.payment.PaymentOutboxHelper
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository
import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.saga.SagaStatus
import com.food.ordering.system.saga.SagaStatus.*
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
                sagaStatus = arrayOf(STARTED)
            )

        if (orderPaymentOutboxResponse == null) {
            logger.info("An outbox message with saga id: ${data.sagaId} is already processed!")
            return
        }

        val domainEvent = completePaymentForOrder(data)

        val sagaStatus = orderSagaHelper.orderStatusToSagaStatus(domainEvent.order.orderStatus)

        paymentOutboxHelper.save(
            getUpdatedPaymentOutboxMessage(
                orderPaymentOutboxResponse = orderPaymentOutboxResponse,
                orderStatus = domainEvent.order.orderStatus,
                sagaStatus = sagaStatus
            )
        )
        approvalOutboxHelper.saveApprovalOutboxMessage(
            orderApprovalEventPayload = orderDataMapper.orderPaidEventToOrderApprovalEventPayload(domainEvent),
            orderStatus = domainEvent.order.orderStatus,
            outboxStatus = OutboxStatus.STARTED,
            sagaStatus = sagaStatus,
            sagaId = UUID.fromString(data.sagaId)
        )

        logger.info("Order with id: ${domainEvent.order.id.value} is paid")
    }


    @Transactional
    override fun rollback(data: PaymentResponse) {
        val orderPaymentOutboxMessage =
            paymentOutboxHelper.getPaymentOutboxMessageBySagaIdAndSagaStatus(
                sagaId = UUID.fromString(data.sagaId),
                sagaStatus = getCurrentSagaStatus(data.paymentStatus)
            ) ?: run {
                logger.info("An outbox message with saga id: ${data.sagaId} is already roll back!")
                return
            }

        val order: Order = rollbackPaymentForOrder(data)

        val sagaStatus = orderSagaHelper.orderStatusToSagaStatus(order.orderStatus)
        paymentOutboxHelper.save(
            getUpdatedPaymentOutboxMessage(
                orderPaymentOutboxResponse = orderPaymentOutboxMessage,
                orderStatus = order.orderStatus,
                sagaStatus = sagaStatus
            )
        )

        if (data.paymentStatus == CANCELLED) {
            approvalOutboxHelper.save(
                getUpdatedApprovalOutboxMessage(
                    data.sagaId,
                    order.orderStatus,
                    sagaStatus
                )
            )
        }

        logger.info("Order with id: ${order.id.value} is cancelled")
    }

    fun findOrder(orderId: String): Order = orderRepository.findById(OrderId(UUID.fromString(orderId)))
        ?: run {
            logger.error("Order with id: $orderId could not be found!")
            throw OrderNotFoundException("Order with id: $orderId could not be found!")
        }

    private fun rollbackPaymentForOrder(data: PaymentResponse): Order {
        logger.info("Cancelling order with id: ${data.orderId}")
        val order = findOrder(data.orderId)
        orderDomainService.cancelOrder(
            order = order,
            failureMessage = order.failureMessages
        )
        orderRepository.save(order)
        return order
    }

    private fun getCurrentSagaStatus(paymentStatus: PaymentStatus): Array<out SagaStatus> {
        return when (paymentStatus) {
            COMPLETED -> arrayOf(STARTED)
            CANCELLED -> arrayOf(PROCESSING)
            FAILED -> arrayOf(STARTED, PROCESSING)
        }
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

    private fun completePaymentForOrder(paymentResponse: PaymentResponse): OrderPaidEvent {
        logger.info("Completing payment for order with id: ${paymentResponse.orderId}")
        val order: Order = orderSagaHelper.findOrder(paymentResponse.orderId)
        val domainEvent = orderDomainService.payOrder(order)
        orderRepository.save(order)
        return domainEvent
    }

    private fun getUpdatedApprovalOutboxMessage(
        sagaId: String,
        orderStatus: OrderStatus,
        sagaStatus: SagaStatus,
    ): OrderApprovalOutboxMessage {
        val orderApprovalOutboxMessage =
            approvalOutboxHelper.getApprovalOutboxMessageBySagaIdAndSagaStatus(
                sagaId = UUID.fromString(sagaId),
                sagaStatus = arrayOf(COMPENSATING)
            ) ?: run {
                throw OrderDomainException("Approval outbox message could not be found! ${COMPENSATING.name} status!")
            }

        orderApprovalOutboxMessage.processedAt = ZonedDateTime.now(ZoneId.of(KOREA_DATE_TIME))
        orderApprovalOutboxMessage.orderStatus = orderStatus
        orderApprovalOutboxMessage.sagaStatus = sagaStatus
        return orderApprovalOutboxMessage
    }
}
