package com.food.ordering.system.payment.service.domain.ports.output.output.repostiroy

import com.food.ordering.system.domain.valueobject.PaymentStatus
import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.payment.service.domain.outbox.model.OrderOutboxMessage
import java.util.UUID

interface OrderOutboxRepository {

    fun save(orderOutboxMessage: OrderOutboxMessage): OrderOutboxMessage

    fun findByTypeAndOutboxStatus(
        type: String,
        status: OutboxStatus,
    ): List<OrderOutboxMessage>

    fun findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(
        type: String,
        sagaId: UUID,
        paymentStatus: PaymentStatus,
        outboxStatus: OutboxStatus,
    ): OrderOutboxMessage?

    fun deleteByTypeAndOutboxStatus(
        type: String,
        status: OutboxStatus,
    )
}
