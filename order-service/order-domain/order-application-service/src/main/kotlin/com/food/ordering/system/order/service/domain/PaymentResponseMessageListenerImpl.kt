package com.food.ordering.system.order.service.domain

import food.ordering.system.order.service.domain.dto.message.PaymentResponse
import food.ordering.system.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PaymentResponseMessageListenerImpl : PaymentResponseMessageListener{

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun paymentCompleted(paymentResponse: PaymentResponse) {
        TODO("Not yet implemented")
    }

    override fun paymentCancelled(paymentResponse: PaymentResponse) {
        TODO("Not yet implemented")
    }
}
