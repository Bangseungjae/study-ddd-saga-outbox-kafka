package com.food.ordering.system.order.service.dataaccess.outbox.payment.adapter

import com.food.ordering.system.order.service.dataaccess.outbox.payment.mapper.toOrderPaymentOutboxMessage
import com.food.ordering.system.order.service.dataaccess.outbox.payment.mapper.toOutboxEntity
import com.food.ordering.system.order.service.dataaccess.outbox.payment.repository.PaymentOutboxJpaRepository
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage
import com.food.ordering.system.order.service.domain.ports.output.repository.PaymentOutboxRepository
import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.saga.SagaStatus
import org.springframework.stereotype.Component
import java.util.*

@Component
private class PaymentOutboxRepositoryImpl(
    private val paymentOutboxJpaRepository: PaymentOutboxJpaRepository,
) : PaymentOutboxRepository {
    override fun save(orderPaymentOutboxMessage: OrderPaymentOutboxMessage): OrderPaymentOutboxMessage =
        paymentOutboxJpaRepository.save(orderPaymentOutboxMessage.toOutboxEntity())
            .toOrderPaymentOutboxMessage()

    override fun findByTypeAndOutboxStatusAndSagaStatus(
        type: String,
        outboxStatus: OutboxStatus,
        vararg sagaStatus: SagaStatus,
    ): List<OrderPaymentOutboxMessage> = paymentOutboxJpaRepository.findByTypeAndOutboxStatusAndSagaStatusIn(
        type = type,
        outboxStatus = outboxStatus,
        sagaStatus = sagaStatus.toList()
    ).map { it.toOrderPaymentOutboxMessage() }
        .toList()

    override fun findByTypeAndSagaIdAndSagaStatus(
        type: String,
        sagaId: UUID,
        vararg sagaStatus: SagaStatus,
    ): OrderPaymentOutboxMessage? {
        return paymentOutboxJpaRepository.findByTypeAndSagaIdAndSagaStatusIn(
            type = type,
            sagaId = sagaId,
            sagaStatus = sagaStatus.toList(),
        )?.toOrderPaymentOutboxMessage()
    }

    override fun deleteByTypeAndOutboxStatusAndSagaStatus(
        type: String,
        outboxStatus: OutboxStatus,
        vararg sagaStatus: SagaStatus,
    ) {
        paymentOutboxJpaRepository.deleteByTypeAndOutboxStatusAndSagaStatusIn(
            type = type,
            outboxStatus = outboxStatus,
            sagaStatus = sagaStatus.toList(),
        )
    }
}
