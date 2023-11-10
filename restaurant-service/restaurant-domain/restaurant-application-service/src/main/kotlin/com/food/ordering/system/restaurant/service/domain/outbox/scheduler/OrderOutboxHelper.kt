package com.food.ordering.system.restaurant.service.domain.outbox.scheduler

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.food.ordering.system.domain.KOREA_DATE_TIME
import com.food.ordering.system.domain.valueobject.OrderApprovalStatus
import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.restaurant.service.domain.exception.RestaurantDomainException
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderEventPayload
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderOutboxMessage
import com.food.ordering.system.restaurant.service.domain.ports.output.repository.OrderOutboxRepository
import com.food.ordering.system.saga.order.ORDER_SAGA_NAME
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

@Component
class OrderOutboxHelper(
    private val orderOutboxRepository: OrderOutboxRepository,
    private val objectMapper: ObjectMapper,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional(readOnly = true)
    fun getCompletedOrderOutboxMessageBySagaIdAndOutboxStatus(
        sagaId: UUID,
        outboxStatus: OutboxStatus,
    ): OrderOutboxMessage? = orderOutboxRepository.findByTypeAndSagaIdAndOutboxStatus(
        type = ORDER_SAGA_NAME,
        sagaId = sagaId,
        outboxStatus = outboxStatus,
    )

    @Transactional(readOnly = true)
    fun getOrderOutboxMessageByOutboxStatus(outboxStatus: OutboxStatus): List<OrderOutboxMessage> =
        orderOutboxRepository.findByTypeAndOutboxStatus(
            type = ORDER_SAGA_NAME,
            outboxStatus = outboxStatus,
        )

    @Transactional
    fun deleteOrderOutboxMessageByOutboxStatus(outboxStatus: OutboxStatus) =
        orderOutboxRepository.deleteByTypeAndOutboxStatus(
            type = ORDER_SAGA_NAME,
            outboxStatus = outboxStatus,
        )

    @Transactional
    fun saveOrderOutboxMessage(
        orderEventPayload: OrderEventPayload,
        approvalStatus: OrderApprovalStatus,
        outboxStatus: OutboxStatus,
        sagaId: UUID,
    ) {
        val orderOutboxMessage = OrderOutboxMessage(
            id = UUID.randomUUID(),
            sagaId = sagaId,
            createdAt = orderEventPayload.createdAt,
            processedAt = ZonedDateTime.now(ZoneId.of(KOREA_DATE_TIME)),
            type = ORDER_SAGA_NAME,
            payload = createPayload(orderEventPayload),
            outboxStatus = outboxStatus,
            approvalStatus = approvalStatus,
            version = 0,
        )
        save(orderOutboxMessage)
    }

    @Transactional
    fun updateOutboxMessage(
        orderPaymentOutboxMessage: OrderOutboxMessage,
        outboxStatus: OutboxStatus,
    ) {
        orderPaymentOutboxMessage.outboxStatus = outboxStatus
        save(orderPaymentOutboxMessage)
        logger.info("Order outbox table is updated as: ${outboxStatus.name}")
    }


    private fun save(orderPaymentOutboxMessage: OrderOutboxMessage) {
        try {
            orderOutboxRepository.save(orderPaymentOutboxMessage)
        } catch (e: Exception) {
            logger.error("Could not save OrderOutboxMessage")
        }

        logger.info("OrderOutboxMessage saved with id: ${orderPaymentOutboxMessage.id}")
    }

    private fun createPayload(orderEventPayload: OrderEventPayload): String {
        try {
            return objectMapper.writeValueAsString(orderEventPayload)
        } catch (e: JsonProcessingException) {
            logger.error("Could not create OrderEventPayload json!", e)
            throw RestaurantDomainException("Could not create OrderEventPayload json!")
        }
    }
}
