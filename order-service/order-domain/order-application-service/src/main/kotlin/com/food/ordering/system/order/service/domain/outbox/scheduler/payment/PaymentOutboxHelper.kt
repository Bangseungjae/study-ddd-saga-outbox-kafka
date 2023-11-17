package com.food.ordering.system.order.service.domain.outbox.scheduler.payment

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.food.ordering.system.domain.valueobject.OrderStatus
import com.food.ordering.system.order.service.domain.exception.OrderDomainException
import com.food.ordering.system.domain.event.payload.OrderPaymentEventPayload
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage
import com.food.ordering.system.order.service.domain.ports.output.repository.PaymentOutboxRepository
import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.saga.SagaStatus
import com.food.ordering.system.saga.order.ORDER_SAGA_NAME
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Component
class PaymentOutboxHelper(
    private val paymentOutboxRepository: PaymentOutboxRepository,
    private val objectMapper: ObjectMapper,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional(readOnly = true)
    fun getPaymentOutboxMessageByOutboxStatusAndSagaStatus(
        outboxStatus: OutboxStatus,
        vararg sagaStatus: SagaStatus,
    ): List<OrderPaymentOutboxMessage> {
        return paymentOutboxRepository.findByTypeAndOutboxStatusAndSagaStatus(
            type = ORDER_SAGA_NAME,
            outboxStatus = outboxStatus,
            sagaStatus = sagaStatus,
        )
    }

    @Transactional(readOnly = true)
    fun getPaymentOutboxMessageBySagaIdAndSagaStatus(
        sagaId: UUID,
        vararg sagaStatus: SagaStatus,
    ): OrderPaymentOutboxMessage? {
        return paymentOutboxRepository.findByTypeAndSagaIdAndSagaStatus(
            type = ORDER_SAGA_NAME,
            sagaId = sagaId,
            sagaStatus = sagaStatus,
        )
    }

    @Transactional
    fun save(orderPaymentOutboxMessage: OrderPaymentOutboxMessage) {
        paymentOutboxRepository.save(orderPaymentOutboxMessage)
        logger.info("OrderPaymentOutboxMessage saved with outbox id: ${orderPaymentOutboxMessage.id}")
    }

    @Transactional
    fun savePaymentOutboxMessage(
        paymentEventPayload: OrderPaymentEventPayload,
        orderStatus: OrderStatus,
        sagaStatus: SagaStatus,
        outboxStatus: OutboxStatus,
        sagaId: UUID,
    ) {
        save(
            OrderPaymentOutboxMessage(
                id = UUID.randomUUID(),
                sagaId = sagaId,
                createdAt = paymentEventPayload.createdAt,
                type = ORDER_SAGA_NAME,
                payload = createPayload(paymentEventPayload),
                orderStatus = orderStatus,
                sagaStatus = sagaStatus,
                outboxStatus = outboxStatus,
            )
        )
    }

    private fun createPayload(paymentEventPayload: OrderPaymentEventPayload): String {

        try {
            return objectMapper.writeValueAsString(paymentEventPayload)
        } catch (e: JsonProcessingException) {
            logger.error("Cloud not create OrderPaymentEventPayload object for order id ${paymentEventPayload.orderId}", e)
            throw OrderDomainException("Cloud not create OrderPaymentEventPayload object for order id ${paymentEventPayload.orderId}")
        }
    }

    @Transactional
    fun deletePaymentOutboxMessageByOutboxStatusAndSagaStatus(
        outboxStatus: OutboxStatus,
        vararg sagaStatus: SagaStatus,
    ) {
        paymentOutboxRepository.deleteByTypeAndOutboxStatusAndSagaStatus(
            sagaStatus = sagaStatus,
            outboxStatus = outboxStatus,
            type = ORDER_SAGA_NAME
        )
    }
}
