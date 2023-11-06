package com.food.ordering.system.payment.service.messaging.mapper

import com.food.ordering.system.domain.valueobject.PaymentOrderStatus
import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel
import com.food.ordering.system.kafka.order.avro.model.PaymentStatus
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest
import com.food.ordering.system.payment.service.domain.event.PaymentCancelledEvent
import com.food.ordering.system.payment.service.domain.event.PaymentCompletedEvent
import com.food.ordering.system.payment.service.domain.event.PaymentFailedEvent
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class PaymentMessagingDataMapper {

    fun paymentCompletedEventToPaymentResponseAvroModel(paymentCompletedEvent: PaymentCompletedEvent): PaymentResponseAvroModel =
        PaymentResponseAvroModel.newBuilder()
            .setId(UUID.randomUUID())
            .setSagaId(UUID.randomUUID())
            .setPaymentId(paymentCompletedEvent.payment.id.value)
            .setCustomerId(paymentCompletedEvent.payment.customerId.value)
            .setOrderId(paymentCompletedEvent.payment.orderId.value)
            .setPrice(paymentCompletedEvent.payment.price.amount)
            .setCreatedAt(paymentCompletedEvent.createdAt.toInstant())
            .setPaymentStatus(PaymentStatus.valueOf(paymentCompletedEvent.payment.paymentStatus.name))
            .setFailureMessages(paymentCompletedEvent.failureMessages)
            .build()

    fun paymentCancelledEventToPaymentResponseAvroModel(paymentCancelledEvent: PaymentCancelledEvent): PaymentResponseAvroModel =
        PaymentResponseAvroModel.newBuilder()
            .setId(UUID.randomUUID())
            .setSagaId(UUID.randomUUID())
            .setPaymentId(paymentCancelledEvent.payment.id.value)
            .setCustomerId(paymentCancelledEvent.payment.customerId.value)
            .setOrderId(paymentCancelledEvent.payment.orderId.value)
            .setPrice(paymentCancelledEvent.payment.price.amount)
            .setCreatedAt(paymentCancelledEvent.createdAt.toInstant())
            .setPaymentStatus(PaymentStatus.valueOf(paymentCancelledEvent.payment.paymentStatus.name))
            .setFailureMessages(paymentCancelledEvent.failureMessages)
            .build()

    fun paymentFailedEventToPaymentResponseAvroModel(paymentFailedEvent: PaymentFailedEvent): PaymentResponseAvroModel =
        PaymentResponseAvroModel.newBuilder()
            .setId(UUID.randomUUID())
            .setSagaId(UUID.randomUUID())
            .setPaymentId(paymentFailedEvent.payment.id.value)
            .setCustomerId(paymentFailedEvent.payment.customerId.value)
            .setOrderId(paymentFailedEvent.payment.orderId.value)
            .setPrice(paymentFailedEvent.payment.price.amount)
            .setCreatedAt(paymentFailedEvent.createdAt.toInstant())
            .setPaymentStatus(PaymentStatus.valueOf(paymentFailedEvent.payment.paymentStatus.name))
            .setFailureMessages(paymentFailedEvent.failureMessages)
            .build()

    fun paymentRequestAvroModelToPaymentRequest(paymentRequestAvroModel: PaymentRequestAvroModel): PaymentRequest =
        PaymentRequest(
            id = paymentRequestAvroModel.id.toString(),
            sagaId = paymentRequestAvroModel.sagaId.toString(),
            orderId = paymentRequestAvroModel.orderId.toString(),
            customerId = paymentRequestAvroModel.customerId.toString(),
            price = paymentRequestAvroModel.price,
            createdAt = paymentRequestAvroModel.createdAt,
            paymentOrderStatus = PaymentOrderStatus.valueOf(paymentRequestAvroModel.paymentOrderStatus.name)
        )
}
