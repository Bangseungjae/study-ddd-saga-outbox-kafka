package food.ordering.system.order.service.domain.ports.input.message.listener.payment

import food.ordering.system.order.service.domain.dto.message.PaymentResponse

interface PaymentResponseMessageListener {

    fun paymentCompleted(paymentResponse: PaymentResponse)

    fun paymentCancelled(paymentResponse: PaymentResponse)
}
