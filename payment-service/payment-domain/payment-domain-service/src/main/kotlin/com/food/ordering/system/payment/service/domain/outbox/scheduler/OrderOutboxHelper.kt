package com.food.ordering.system.payment.service.domain.outbox.scheduler

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.food.ordering.system.domain.KOREA_DATE_TIME
import com.food.ordering.system.domain.valueobject.PaymentStatus
import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.payment.service.domain.exception.PaymentDomainException
import com.food.ordering.system.payment.service.domain.outbox.model.OrderEventPayload
import com.food.ordering.system.payment.service.domain.outbox.model.OrderOutboxMessage
import com.food.ordering.system.payment.service.domain.ports.output.output.repostiroy.OrderOutboxRepository
import com.food.ordering.system.saga.order.ORDER_SAGA_NAME
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

@Component
class OrderOutboxHelper(
    private val objectMapper: ObjectMapper,
    private val orderOutboxRepository: OrderOutboxRepository,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional(readOnly = true)
    fun getCompletedOrderOutboxMessageBySagaIdAndPaymentStatus(
        sagaId: UUID,
        paymentStatus: PaymentStatus,
    ): OrderOutboxMessage? = orderOutboxRepository.findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(
        type = ORDER_SAGA_NAME,
        sagaId = sagaId,
        paymentStatus = paymentStatus,
        outboxStatus = OutboxStatus.COMPLETED,
    )

    @Transactional(readOnly = true)
    fun getOrderOutboxMessageByOutboxStatus(outboxStatus: OutboxStatus): List<OrderOutboxMessage> =
        orderOutboxRepository.findByTypeAndOutboxStatus(
            type = ORDER_SAGA_NAME,
            status = outboxStatus,
        )

    @Transactional
    fun saveOrderOutboxMessage(
        orderEventPayload: OrderEventPayload,
        paymentStatus: PaymentStatus,
        outboxStatus: OutboxStatus,
        sagaId: UUID,
    ) {
        save(
            OrderOutboxMessage(
                id = UUID.randomUUID(),
                sagaId = sagaId,
                createdAt = orderEventPayload.createdAt,
                type = ORDER_SAGA_NAME,
                payload = createPayload(orderEventPayload),
                processedAt = ZonedDateTime.now(ZoneId.of(KOREA_DATE_TIME)),
                paymentStatus = paymentStatus,
                outboxStatus = outboxStatus,
                version = 0,
            )
        )
    }

    @Transactional
    fun updateOutboxMessage(
        orderOutboxMessage: OrderOutboxMessage,
        outboxStatus: OutboxStatus,
    ) {
        orderOutboxMessage.outboxStatus = outboxStatus
        save(orderOutboxMessage)
        logger.info("Order outbox table status is updated as: ${outboxStatus.name}")
    }

    @Transactional
    fun deleteOrderOutboxMessageByOutboxStatus(
        outboxStatus: OutboxStatus,
    ) {
        orderOutboxRepository.deleteByTypeAndOutboxStatus(
            type = ORDER_SAGA_NAME,
            status = outboxStatus,
        )
    }


    private fun save(
        orderOutboxMessage: OrderOutboxMessage,
    ) {
        try {
            val response = orderOutboxRepository.save(orderOutboxMessage)
            logger.info("OrderOutboxMessage is saved with id: ${response.id}")
        } catch (e: Exception) {
            logger.error("Could not save OrderOutboxMessage!")
            throw PaymentDomainException("Could not save OrderOutboxMessage!")
        }
    }

    private fun createPayload(orderEventPayload: OrderEventPayload): String {
        try {
            return objectMapper.writeValueAsString(orderEventPayload)
        } catch (e: JsonProcessingException) {
            throw PaymentDomainException("Could not create OrderEventPayload json!")
        }
    }
}
