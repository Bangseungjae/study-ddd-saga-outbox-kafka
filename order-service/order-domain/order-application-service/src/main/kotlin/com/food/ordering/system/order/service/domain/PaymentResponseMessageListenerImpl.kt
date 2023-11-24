package com.food.ordering.system.order.service.domain

import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse
import com.food.ordering.system.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
private class PaymentResponseMessageListenerImpl(
    private val orderPaymentSaga: OrderPaymentSaga,
) : PaymentResponseMessageListener {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun paymentCompleted(paymentResponse: PaymentResponse) {
        orderPaymentSaga.process(paymentResponse)
        logger.info("Publishing OrderPaidEvent for order id: ${paymentResponse.orderId}")
    }

    override fun paymentCancelled(paymentResponse: PaymentResponse) {
        orderPaymentSaga.rollback(paymentResponse)
        logger.info("Order is roll back for order id: ${paymentResponse.orderId} with failure messages: " +
                "${paymentResponse.failureMessages.joinToString { "," }}")
    }
}
