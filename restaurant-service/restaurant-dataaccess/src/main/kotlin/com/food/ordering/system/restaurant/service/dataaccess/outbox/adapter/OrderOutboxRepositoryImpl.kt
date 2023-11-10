package com.food.ordering.system.restaurant.service.dataaccess.outbox.adapter

import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.restaurant.service.dataaccess.outbox.mapper.toOrderOutboxMessage
import com.food.ordering.system.restaurant.service.dataaccess.outbox.mapper.toOutboxEntity
import com.food.ordering.system.restaurant.service.dataaccess.outbox.repository.OrderOutboxJpaRepository
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderOutboxMessage
import com.food.ordering.system.restaurant.service.domain.ports.output.repository.OrderOutboxRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
class OrderOutboxRepositoryImpl(
    private val orderOutboxJpaRepository: OrderOutboxJpaRepository,
) : OrderOutboxRepository {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun save(orderOutboxMessage: OrderOutboxMessage): OrderOutboxMessage =
        orderOutboxJpaRepository.save(orderOutboxMessage.toOutboxEntity())
            .toOrderOutboxMessage()

    override fun findByTypeAndOutboxStatus(type: String, outboxStatus: OutboxStatus): List<OrderOutboxMessage> =
        orderOutboxJpaRepository.findByTypeAndOutboxStatus(
            type = type,
            outboxStatus = outboxStatus
        ).map { it.toOrderOutboxMessage() }

    override fun findByTypeAndSagaIdAndOutboxStatus(
        type: String,
        sagaId: UUID,
        outboxStatus: OutboxStatus,
    ): OrderOutboxMessage? = orderOutboxJpaRepository.findByTypeAndSagaIdAndOutboxStatus(
        type = type,
        sagaId = sagaId,
        outboxStatus = outboxStatus,
    )?.toOrderOutboxMessage()

    override fun deleteByTypeAndOutboxStatus(type: String, outboxStatus: OutboxStatus) =
        orderOutboxJpaRepository.deleteByTypeAndOutboxStatus(
            type = type,
            outboxStatus = outboxStatus,
        )
}
