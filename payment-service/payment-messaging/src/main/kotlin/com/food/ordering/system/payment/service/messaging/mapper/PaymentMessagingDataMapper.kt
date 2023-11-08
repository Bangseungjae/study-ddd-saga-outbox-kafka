package com.food.ordering.system.payment.service.messaging.mapper

import com.food.ordering.system.domain.valueobject.PaymentOrderStatus
import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel
import com.food.ordering.system.kafka.order.avro.model.PaymentStatus
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest
import com.food.ordering.system.payment.service.domain.outbox.model.OrderEventPayload
import java.util.UUID


fun OrderEventPayload.toPaymentResponseAvroModel(
    sagaId: String,
): PaymentResponseAvroModel  = run {
    PaymentResponseAvroModel.newBuilder()
        .setId(UUID.randomUUID())
        .setSagaId(UUID.fromString(sagaId))
        .setPaymentId(UUID.fromString(paymentId))
        .setCustomerId(UUID.fromString(customerId))
        .setOrderId(UUID.fromString(customerId))
        .setPrice(price)
        .setCreatedAt(createdAt.toInstant())
        .setPaymentStatus(PaymentStatus.valueOf(paymentStatus))
        .setFailureMessages(failureMessages)
        .build()
}

fun PaymentRequestAvroModel.toPaymentRequest(): PaymentRequest = run {
    PaymentRequest(
        id = id.toString(),
        sagaId = sagaId.toString(),
        orderId = orderId.toString(),
        customerId = customerId.toString(),
        price = price,
        createdAt = createdAt,
        paymentOrderStatus = PaymentOrderStatus.valueOf(paymentOrderStatus.name)
    )
}
