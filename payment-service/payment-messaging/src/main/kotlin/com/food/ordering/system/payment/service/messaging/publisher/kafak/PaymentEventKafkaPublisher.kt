package com.food.ordering.system.payment.service.messaging.publisher.kafak

import com.food.ordering.system.kafka.config.producer.KafkaMessageHelper
import com.food.ordering.system.kafka.config.producer.service.KafkaProducer
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel
import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.payment.service.domain.config.PaymentServiceConfigData
import com.food.ordering.system.payment.service.domain.outbox.model.OrderEventPayload
import com.food.ordering.system.payment.service.domain.outbox.model.OrderOutboxMessage
import com.food.ordering.system.payment.service.domain.ports.output.output.message.publisher.PaymentResponseMessagePublisher
import com.food.ordering.system.payment.service.messaging.mapper.toPaymentResponseAvroModel
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.function.BiConsumer

@Component
class PaymentEventKafkaPublisher(
    private val kafkaProducer: KafkaProducer<String, PaymentResponseAvroModel>,
    private val paymentServiceConfigData: PaymentServiceConfigData,
    private val kafkaMessageHelper: KafkaMessageHelper,
) : PaymentResponseMessagePublisher {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun publish(
        orderOutboxMessage: OrderOutboxMessage,
        callback: BiConsumer<OrderOutboxMessage, OutboxStatus>,
    ) {
        val orderEventPayload =
            kafkaMessageHelper.getOrderEventPayload(orderOutboxMessage.payload, OrderEventPayload::class.java)

        val sagaId = orderOutboxMessage.sagaId.toString()

        logger.info("Received OrderOutboxMessage for order id: ${orderEventPayload.orderId} and saga id: $sagaId}")

        try {
            val paymentResponseAvroModel = orderEventPayload.toPaymentResponseAvroModel(sagaId)

            kafkaProducer.send(
                topicName = paymentServiceConfigData.paymentResponseTopicName,
                key = sagaId,
                message = paymentResponseAvroModel,
                callback = kafkaMessageHelper.getKafkaCallback(
                    responseTopicName = paymentServiceConfigData.paymentResponseTopicName,
                    avroModel = paymentResponseAvroModel,
                    outboxMessage = orderOutboxMessage,
                    outboxCallback = callback,
                    orderId = orderEventPayload.orderId,
                    avroModelName = "PaymentResponseAvroModel"
                )
            )

            logger.info(
                "PaymentResponseAvroModel sent to kafka for order id: ${paymentResponseAvroModel.orderId} " +
                        "and saga id: $sagaId"
            )
        } catch (e: Exception) {
            logger.error("Error while sending PaymentRequestAvroModel message to kafka with order id: " +
                    "${orderEventPayload.orderId} and saga id: $sagaId, error ${e.message}")
        }
    }
}
