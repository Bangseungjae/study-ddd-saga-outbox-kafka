package com.food.ordering.system.payment.service.dataaccess.outbox.adapter

import com.food.ordering.system.domain.valueobject.PaymentStatus
import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.payment.service.dataaccess.outbox.mapper.toOrderOutboxMessage
import com.food.ordering.system.payment.service.dataaccess.outbox.mapper.toOutboxMessage
import com.food.ordering.system.payment.service.dataaccess.outbox.repository.OrderOutboxJpaRepository
import com.food.ordering.system.payment.service.domain.outbox.model.OrderOutboxMessage
import com.food.ordering.system.payment.service.domain.ports.output.output.repostiroy.OrderOutboxRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
class OrderOutboxRepositoryImpl(
    private val orderOutboxJpaRepository: OrderOutboxJpaRepository,
) : OrderOutboxRepository {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun save(orderOutboxMessage: OrderOutboxMessage): OrderOutboxMessage =
        orderOutboxJpaRepository.save(orderOutboxMessage.toOutboxMessage())
            .toOrderOutboxMessage()

    override fun findByTypeAndOutboxStatus(type: String, status: OutboxStatus): List<OrderOutboxMessage> =
        orderOutboxJpaRepository.findByTypeAndOutboxStatus(
            type = type,
            outboxStatus = status
        ).map { it.toOrderOutboxMessage() }

    override fun findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(
        type: String,
        sagaId: UUID,
        paymentStatus: PaymentStatus,
        outboxStatus: OutboxStatus,
    ): OrderOutboxMessage? = orderOutboxJpaRepository.findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(
        type = type,
        sagaId = sagaId,
        paymentStatus = paymentStatus,
        outboxStatus = outboxStatus,
    )?.toOrderOutboxMessage()

    override fun deleteByTypeAndOutboxStatus(type: String, status: OutboxStatus) {
        orderOutboxJpaRepository.deleteByTypeAndOutboxStatus(
            type = type,
            outboxStatus = status,
        )
    }
}
