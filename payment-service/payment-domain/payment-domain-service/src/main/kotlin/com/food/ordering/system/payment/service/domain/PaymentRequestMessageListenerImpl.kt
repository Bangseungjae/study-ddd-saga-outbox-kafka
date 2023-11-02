package com.food.ordering.system.payment.service.domain

import com.food.ordering.system.payment.service.domain.dto.PaymentRequest
import com.food.ordering.system.payment.service.domain.event.PaymentCancelledEvent
import com.food.ordering.system.payment.service.domain.event.PaymentCompletedEvent
import com.food.ordering.system.payment.service.domain.event.PaymentEvent
import com.food.ordering.system.payment.service.domain.event.PaymentFailedEvent
import com.food.ordering.system.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener
import com.food.ordering.system.payment.service.domain.ports.output.output.message.publisher.PaymentCancelledMessagePublisher
import com.food.ordering.system.payment.service.domain.ports.output.output.message.publisher.PaymentCompletedMessagePublisher
import com.food.ordering.system.payment.service.domain.ports.output.output.message.publisher.PaymentFailedMessagePublisher
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PaymentRequestMessageListenerImpl(
    private val paymentRequestHelper: PaymentRequestHelper,
    private val paymentCompletedMessagePublisher: PaymentCompletedMessagePublisher,
    private val paymentCancelledMessagePublisher: PaymentCancelledMessagePublisher,
    private val paymentFailedMessagePublisher: PaymentFailedMessagePublisher,
) : PaymentRequestMessageListener {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun completePayment(paymentRequest: PaymentRequest) {
        val paymentEvent = paymentRequestHelper.persistPayment(paymentRequest)
        fireEvent(paymentEvent)
    }

    override fun cancelPayment(paymentRequest: PaymentRequest) {
        val paymentEvent = paymentRequestHelper.persistCancelPayment(paymentRequest)
        fireEvent(paymentEvent)
    }

    private fun fireEvent(paymentEvent: PaymentEvent) {
        logger.info("Publishing payment event with payment id: ${paymentEvent.payment.id} " +
                "and order id: ${paymentEvent.payment.orderId.id}")

        when (paymentEvent) {
            is PaymentCompletedEvent -> paymentCompletedMessagePublisher.publish(paymentEvent)
            is PaymentCancelledEvent -> paymentCancelledMessagePublisher.publish(paymentEvent)
            is PaymentFailedEvent -> paymentFailedMessagePublisher.publish(paymentEvent)
        }
    }
}
