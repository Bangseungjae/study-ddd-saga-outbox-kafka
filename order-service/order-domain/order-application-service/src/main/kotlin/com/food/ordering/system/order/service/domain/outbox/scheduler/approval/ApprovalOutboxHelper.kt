package com.food.ordering.system.order.service.domain.outbox.scheduler.approval

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.food.ordering.system.domain.valueobject.OrderStatus
import com.food.ordering.system.order.service.domain.exception.OrderDomainException
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalEventPayload
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage
import com.food.ordering.system.order.service.domain.ports.output.repository.ApprovalOutboxRepository
import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.saga.SagaStatus
import com.food.ordering.system.saga.order.ORDER_SAGA_NAME
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Component
class ApprovalOutboxHelper(
    private val approvalOutboxRepository: ApprovalOutboxRepository,
    private val objectMapper: ObjectMapper,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional(readOnly = true)
    fun getApprovalOutboxMessageByOutboxStatusAndSagaStatus(
        outboxStatus: OutboxStatus,
        vararg sagaStatus: SagaStatus,
    ): List<OrderApprovalOutboxMessage> {
        return approvalOutboxRepository.findByTypeAndOutboxStatusAndSagaStatus(
            type = ORDER_SAGA_NAME,
            outboxStatus = outboxStatus,
            sagaStatus = sagaStatus
        )
    }

    @Transactional(readOnly = true)
    fun getApprovalOutboxMessageBySagaIdAndSagaStatus(
        sagaId: UUID,
        vararg sagaStatus: SagaStatus,
    ): OrderApprovalOutboxMessage? {
        return approvalOutboxRepository.findByTypeAndSagaIdAndSagaStatus(
            type = ORDER_SAGA_NAME,
            sagaId = sagaId,
            sagaStatus = sagaStatus
        )
    }

    @Transactional
    fun save(orderApprovalOutboxMessage: OrderApprovalOutboxMessage) {

        try {
            val response = approvalOutboxRepository.save(orderApprovalOutboxMessage)
        } catch (e: Exception) {
            logger.error("Could not save OrderApprovalOutboxMessage with outbox id: ${orderApprovalOutboxMessage.id}")
            throw OrderDomainException("Could not save OrderApprovalOutboxMessage with outbox id: ${orderApprovalOutboxMessage.id}")
        }
        logger.info("OrderApprovalOutboxMessage saved with outbox id: ${orderApprovalOutboxMessage.id}")
    }

    @Transactional
    fun deleteApprovalOutboxMessageByOutboxStatusAndSagaStatus(
        outboxStatus: OutboxStatus,
        vararg sagaStatus: SagaStatus,
    ) {
        approvalOutboxRepository.deleteByTypeAndOutboxStatusAndSagaStatus(
            type = ORDER_SAGA_NAME,
            outboxStatus = outboxStatus,
            sagaStatus = sagaStatus,
        )
    }

    @Transactional
    fun saveApprovalOutboxMessage(
        orderApprovalEventPayload: OrderApprovalEventPayload,
        orderStatus: OrderStatus,
        sagaStatus: SagaStatus,
        outboxStatus: OutboxStatus,
        sagaId: UUID,

        ) {
        save(
            OrderApprovalOutboxMessage(
                id = UUID.randomUUID(),
                sagaId = sagaId,
                createdAt = orderApprovalEventPayload.createdAt,
                type = ORDER_SAGA_NAME,
                payload = createPayload(orderApprovalEventPayload),
                sagaStatus = sagaStatus,
                orderStatus = orderStatus,
                outboxStatus = outboxStatus,
                version = 2,
            )
        )
    }

    private fun createPayload(orderApprovalEventPayload: OrderApprovalEventPayload): String {

        try {
            return objectMapper.writeValueAsString(orderApprovalEventPayload)
        } catch (e: JsonProcessingException) {
            logger.error(
                "Could not create OrderApprovalEventPayload for order id: ${orderApprovalEventPayload.orderId}",
                e
            )
            throw OrderDomainException("Could not create OrderApprovalEventPayload for order id: ${orderApprovalEventPayload.orderId}")
        }

    }
}
