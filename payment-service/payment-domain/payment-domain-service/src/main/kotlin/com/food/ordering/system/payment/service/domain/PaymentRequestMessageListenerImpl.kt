package com.food.ordering.system.payment.service.domain

import com.food.ordering.system.payment.service.domain.dto.PaymentRequest
import com.food.ordering.system.payment.service.domain.event.PaymentCancelledEvent
import com.food.ordering.system.payment.service.domain.event.PaymentCompletedEvent
import com.food.ordering.system.payment.service.domain.event.PaymentEvent
import com.food.ordering.system.payment.service.domain.event.PaymentFailedEvent
import com.food.ordering.system.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PaymentRequestMessageListenerImpl(
    private val paymentRequestHelper: PaymentRequestHelper,
) : PaymentRequestMessageListener {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun completePayment(paymentRequest: PaymentRequest) {
        paymentRequestHelper.persistPayment(paymentRequest)
    }

    override fun cancelPayment(paymentRequest: PaymentRequest) {
        paymentRequestHelper.persistCancelPayment(paymentRequest)
    }
}
