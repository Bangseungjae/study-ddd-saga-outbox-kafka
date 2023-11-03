package com.food.ordering.system.payment.service.messaging.listener

import com.food.ordering.system.kafka.consumer.KafkaConsumer
import com.food.ordering.system.kafka.order.avro.model.PaymentOrderStatus
import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel
import com.food.ordering.system.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener
import com.food.ordering.system.payment.service.messaging.mapper.PaymentMessagingDataMapper
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class PaymentRequestKafkaListener(
    private val paymentRequestMessageListener: PaymentRequestMessageListener,
    private val paymentMessagingDataMapper: PaymentMessagingDataMapper,
) : KafkaConsumer<PaymentRequestAvroModel> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(
        id = "\${kafka-consumer-config.payment-consumer-group-id}",
        topics = ["\${payment-service.payment-request-topic-name}"],
    )
    override fun receive(
        @Payload messages: List<PaymentRequestAvroModel>,
        @Header(KafkaHeaders.RECEIVED_KEY) keys: List<String>,
        @Header(KafkaHeaders.RECEIVED_PARTITION) partitions: List<Int>,
        @Header(KafkaHeaders.OFFSET) offsets: List<Long>,
    ) {
        logger.info("${messages.size} number of payment requests received with keys: ${keys.toString()}, " +
                "partitions: ${partitions.toString()} and offsets: ${offsets.toString()}")

        messages.forEach {paymentRequestAvroModel ->
            if (PaymentOrderStatus.PENDING == paymentRequestAvroModel.getPaymentOrderStatus()) {
                logger.info("Processing payment for order id: ${paymentRequestAvroModel.getOrderId()}")
                paymentRequestMessageListener.completePayment(
                    paymentMessagingDataMapper.paymentRequestAvroModelToPaymentRequest(paymentRequestAvroModel)
                )
            } else if (PaymentOrderStatus.CANCELLED == paymentRequestAvroModel.getPaymentOrderStatus()) {
                logger.info("Cancelling payment for order id: ${paymentRequestAvroModel.getOrderId()}")
                paymentRequestMessageListener.cancelPayment(
                    paymentMessagingDataMapper.paymentRequestAvroModelToPaymentRequest(paymentRequestAvroModel)
                )
            }
        }
    }
}
