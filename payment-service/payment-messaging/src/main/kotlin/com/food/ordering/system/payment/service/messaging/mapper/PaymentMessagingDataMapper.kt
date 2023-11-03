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
            .setId(UUID.randomUUID().toString())
            .setSagaId("")
            .setPaymentId(paymentCompletedEvent.payment.id.value.toString())
            .setCustomerId(paymentCompletedEvent.payment.customerId.value.toString())
            .setOrderId(paymentCompletedEvent.payment.orderId.value.toString())
            .setPrice(paymentCompletedEvent.payment.price.amount)
            .setCreatedAt(paymentCompletedEvent.createdAt.toInstant())
            .setPaymentStatus(PaymentStatus.valueOf(paymentCompletedEvent.payment.paymentStatus.name))
            .setFailureMessages(paymentCompletedEvent.failureMessages)
            .build()

    fun paymentCancelledEventToPaymentResponseAvroModel(paymentCancelledEvent: PaymentCancelledEvent): PaymentResponseAvroModel =
        PaymentResponseAvroModel.newBuilder()
            .setId(UUID.randomUUID().toString())
            .setSagaId("")
            .setPaymentId(paymentCancelledEvent.payment.id.value.toString())
            .setCustomerId(paymentCancelledEvent.payment.customerId.value.toString())
            .setOrderId(paymentCancelledEvent.payment.orderId.value.toString())
            .setPrice(paymentCancelledEvent.payment.price.amount)
            .setCreatedAt(paymentCancelledEvent.createdAt.toInstant())
            .setPaymentStatus(PaymentStatus.valueOf(paymentCancelledEvent.payment.paymentStatus.name))
            .setFailureMessages(paymentCancelledEvent.failureMessages)
            .build()

    fun paymentFailedEventToPaymentResponseAvroModel(paymentFailedEvent: PaymentFailedEvent): PaymentResponseAvroModel =
        PaymentResponseAvroModel.newBuilder()
            .setId(UUID.randomUUID().toString())
            .setSagaId("")
            .setPaymentId(paymentFailedEvent.payment.id.value.toString())
            .setCustomerId(paymentFailedEvent.payment.customerId.value.toString())
            .setOrderId(paymentFailedEvent.payment.orderId.value.toString())
            .setPrice(paymentFailedEvent.payment.price.amount)
            .setCreatedAt(paymentFailedEvent.createdAt.toInstant())
            .setPaymentStatus(PaymentStatus.valueOf(paymentFailedEvent.payment.paymentStatus.name))
            .setFailureMessages(paymentFailedEvent.failureMessages)
            .build()

    fun paymentRequestAvroModelToPaymentRequest(paymentRequestAvroModel: PaymentRequestAvroModel): PaymentRequest =
        PaymentRequest(
            id = paymentRequestAvroModel.getId(),
            sagaId = paymentRequestAvroModel.getSagaId(),
            orderId = paymentRequestAvroModel.getOrderId(),
            customerId = paymentRequestAvroModel.getCustomerId(),
            price = paymentRequestAvroModel.getPrice(),
            createdAt = paymentRequestAvroModel.getCreatedAt(),
            paymentOrderStatus = PaymentOrderStatus.valueOf(paymentRequestAvroModel.getPaymentOrderStatus().name)
        )
}
