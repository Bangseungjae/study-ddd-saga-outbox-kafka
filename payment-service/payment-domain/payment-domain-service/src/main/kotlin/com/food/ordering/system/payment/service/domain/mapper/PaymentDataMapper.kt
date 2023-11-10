package com.food.ordering.system.payment.service.domain.mapper

import com.food.ordering.system.domain.valueobject.CustomerId
import com.food.ordering.system.domain.valueobject.Money
import com.food.ordering.system.domain.valueobject.OrderId
import com.food.ordering.system.domain.valueobject.PaymentStatus
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest
import com.food.ordering.system.payment.service.domain.entity.Payment
import com.food.ordering.system.payment.service.domain.event.PaymentEvent
import com.food.ordering.system.payment.service.domain.outbox.model.OrderEventPayload
import org.springframework.stereotype.Component
import java.util.*

@Component
class PaymentDataMapper {
    fun paymentRequestModelToPayment(paymentRequest: PaymentRequest): Payment = Payment(
        orderId =  OrderId(UUID.fromString(paymentRequest.orderId)),
        customerId = CustomerId(UUID.fromString(paymentRequest.customerId)),
        price = Money(paymentRequest.price),
        paymentStatus = PaymentStatus.COMPLETED,
    )

    fun paymentEventToOrderEventPayload(paymentEvent: PaymentEvent): OrderEventPayload {
        return OrderEventPayload(
            paymentId = paymentEvent.payment.id.value.toString(),
            customerId = paymentEvent.payment.customerId.value.toString(),
            orderId = paymentEvent.payment.orderId.value.toString(),
            price = paymentEvent.payment.price.amount,
            createdAt = paymentEvent.createdAt,
            paymentStatus = paymentEvent.payment.paymentStatus.name,
            failureMessages = paymentEvent.failureMessages,
        )
    }

}
