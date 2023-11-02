package com.food.ordering.system.payment.service.dataaccess.payment.mapper

import com.food.ordering.system.domain.valueobject.CustomerId
import com.food.ordering.system.domain.valueobject.Money
import com.food.ordering.system.domain.valueobject.OrderId
import com.food.ordering.system.payment.service.dataaccess.payment.entity.PaymentEntity
import com.food.ordering.system.payment.service.domain.entity.Payment
import com.food.ordering.system.payment.service.domain.valueobject.PaymentId
import org.springframework.stereotype.Component

@Component
class PaymentDataAccessMapper {

    fun paymentToPaymentEntity(payment: Payment): PaymentEntity = PaymentEntity(
        id = payment.id.value,
        customerId = payment.customerId.id,
        orderId = payment.orderId.id,
        price = payment.price.amount,
        status = payment.paymentStatus,
        createdAt = payment.createdAt
    )

    fun paymentEntityToPayment(paymentEntity: PaymentEntity): Payment = Payment(
        paymentId = PaymentId(paymentEntity.id),
        orderId = OrderId(paymentEntity.orderId),
        customerId = CustomerId(paymentEntity.customerId),
        price = Money(paymentEntity.price),
        paymentStatus = paymentEntity.status,
        createdAt = paymentEntity.createdAt,
    )
}
