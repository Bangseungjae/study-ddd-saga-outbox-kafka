package com.food.ordering.system.payment.service.messaging.publisher.kafak

import com.food.ordering.system.kafka.config.producer.KafkaMessageHelper
import com.food.ordering.system.kafka.config.producer.service.KafkaProducer
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel
import com.food.ordering.system.payment.service.domain.config.PaymentServiceConfigData
import com.food.ordering.system.payment.service.domain.event.PaymentCancelledEvent
import com.food.ordering.system.payment.service.domain.event.PaymentCompletedEvent
import com.food.ordering.system.payment.service.domain.event.PaymentFailedEvent
import com.food.ordering.system.payment.service.domain.ports.output.output.message.publisher.PaymentCancelledMessagePublisher
import com.food.ordering.system.payment.service.domain.ports.output.output.message.publisher.PaymentCompletedMessagePublisher
import com.food.ordering.system.payment.service.domain.ports.output.output.message.publisher.PaymentFailedMessagePublisher
import com.food.ordering.system.payment.service.messaging.mapper.PaymentMessagingDataMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class PaymentFailedKafkaMessagePublisher(
    private val paymentMessagingDataMapper: PaymentMessagingDataMapper,
    private val kafkaProducer: KafkaProducer<String, PaymentResponseAvroModel>,
    private val paymentServiceConfigData: PaymentServiceConfigData,
    private val kafkaMessageHelper: KafkaMessageHelper,
) : PaymentFailedMessagePublisher {

    private val logger = LoggerFactory.getLogger(this::class.java)
    override fun publish(domainEvent: PaymentFailedEvent) {

        val orderId = domainEvent.payment.orderId.value.toString()

        logger.info("Received PaymentFailed Event for order id: $orderId")
        val paymentResponseAvroModel =
            paymentMessagingDataMapper.paymentFailedEventToPaymentResponseAvroModel(domainEvent)

        try {
            kafkaProducer.send(
                topicName = paymentServiceConfigData.paymentRequestTopicName,
                key = orderId,
                callback = kafkaMessageHelper.getKafkaCallback(
                    paymentServiceConfigData.paymentRequestTopicName,
                    paymentResponseAvroModel,
                    orderId,
                    "PaymentResponseAvroModel",
                ),
                message = paymentResponseAvroModel,
            )
            logger.info("PaymentResponseAvroModel sent to kafka for order id: $orderId")
        } catch (e: Exception) {
            logger.error("Error while sending PaymentResponseAvroModel message to kafka with order id: $orderId, " +
                    "message: ${e.message}")
        }


    }
}
