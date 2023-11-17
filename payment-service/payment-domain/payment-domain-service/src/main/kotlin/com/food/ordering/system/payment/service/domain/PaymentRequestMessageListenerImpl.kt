package com.food.ordering.system.payment.service.domain

import com.food.ordering.system.payment.service.domain.dto.PaymentRequest
import com.food.ordering.system.payment.service.domain.exception.PaymentApplicationServiceException
import com.food.ordering.system.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener
import org.slf4j.LoggerFactory
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Service
import java.util.function.Function

@Service
class PaymentRequestMessageListenerImpl(
    private val paymentRequestHelper: PaymentRequestHelper,
) : PaymentRequestMessageListener {

    private val logger = LoggerFactory.getLogger(this::class.java)
    companion object {
        val MAX_EXECUTION = 100
    }

    override fun completePayment(paymentRequest: PaymentRequest) {
        processPayment(
            func = paymentRequestHelper::persistPayment,
            paymentRequest = paymentRequest,
            methodName = "completePayment"
        )
    }

    override fun cancelPayment(paymentRequest: PaymentRequest) {
        processPayment(
            func = paymentRequestHelper::persistCancelPayment,
            paymentRequest = paymentRequest,
            methodName = "cancelPayment"
        )
    }

    private fun processPayment(
        func: Function<PaymentRequest, Boolean>,
        paymentRequest: PaymentRequest,
        methodName: String,
    ) {
        val execution = 1
        var result: Boolean

        do {
            try {
                result = func.apply(paymentRequest)
                execution.inc()
            } catch (e: OptimisticLockingFailureException) {
                logger.warn("Caught OptimisticLockingFailureException exception in $methodName with message " +
                        "${e.message}!. Retrying for order id: ${paymentRequest.orderId}!")
                result = false
            }
        } while (!result && execution < MAX_EXECUTION)

        if (!result) {
            throw PaymentApplicationServiceException("Could not complete $methodName operation. Throwing exception!")
        }
    }
}
