package com.food.ordering.system.payment.service.domain.entity

import com.food.ordering.system.domain.KOREA_DATE_TIME
import com.food.ordering.system.domain.entity.AggregateRoot
import com.food.ordering.system.domain.valueobject.CustomerId
import com.food.ordering.system.domain.valueobject.Money
import com.food.ordering.system.domain.valueobject.OrderId
import com.food.ordering.system.domain.valueobject.PaymentStatus
import com.food.ordering.system.payment.service.domain.valueobject.PaymentId
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

class Payment(
    paymentId: PaymentId = PaymentId(UUID.randomUUID()),
    val orderId: OrderId,
    val customerId: CustomerId,
    val price: Money,

    var paymentStatus: PaymentStatus,
    val createdAt: ZonedDateTime = ZonedDateTime.now(ZoneId.of(KOREA_DATE_TIME)),

    ) : AggregateRoot<PaymentId>(paymentId) {

    fun validatePayment(failureMessages: MutableList<String>) {
        if (!price.isGreaterThanZero()) {
            failureMessages.add("Total price must be greater than zero!")
        }
    }

    fun updateStatus(paymentStatus: PaymentStatus) {
        this.paymentStatus = paymentStatus
    }
}

