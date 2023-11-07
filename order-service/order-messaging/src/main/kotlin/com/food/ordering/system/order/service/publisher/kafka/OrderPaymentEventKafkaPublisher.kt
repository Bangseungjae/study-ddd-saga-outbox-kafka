package com.food.ordering.system.order.service.publisher.kafka

import com.food.ordering.system.kafka.config.producer.KafkaMessageHelper
import com.food.ordering.system.kafka.config.producer.service.KafkaProducer
import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel
import com.food.ordering.system.order.service.domain.config.OrderServiceConfigData
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentEventPayload
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.PaymentRequestMessagePublisher
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper
import com.food.ordering.system.outbox.OutboxStatus
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.function.BiConsumer

@Component
class OrderPaymentEventKafkaPublisher(
    private val orderMessagingDataMapper: OrderMessagingDataMapper,
    private val kafkaProducer: KafkaProducer<String, PaymentRequestAvroModel>,
    private val orderServiceConfigData: OrderServiceConfigData,
    private val kafkaMessageHelper: KafkaMessageHelper,
) : PaymentRequestMessagePublisher {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun publish(
        orderPaymentOutboxMessage: OrderPaymentOutboxMessage,
        outboxCallback: BiConsumer<OrderPaymentOutboxMessage, OutboxStatus>,
    ) {
        val orderPaymentEventPayload: OrderPaymentEventPayload =
            kafkaMessageHelper.getOrderEventPayload(
                payload =  orderPaymentOutboxMessage.payload,
                outputType =  OrderPaymentEventPayload::class.java,
            )

        val sagaId = orderPaymentOutboxMessage.sagaId.toString()

        logger.info(
            "Received OrderPaymentOutboxMessage for order id:" +
                    " ${orderPaymentEventPayload.orderId} and saga id: $sagaId"
        )

        try {
            val paymentRequestAvroModel = orderMessagingDataMapper
                .orderPaymentEventToPaymentRequestAvroModel(sagaId, orderPaymentEventPayload)

            kafkaProducer.send(
                topicName = orderServiceConfigData.paymentRequestTopicName,
                key = sagaId,
                message = paymentRequestAvroModel,
                callback = kafkaMessageHelper.getKafkaCallback(
                    responseTopicName = orderServiceConfigData.paymentRequestTopicName,
                    avroModelName = "PaymentRequestAvroModel",
                    avroModel = paymentRequestAvroModel,
                    outboxMessage = orderPaymentOutboxMessage,
                    orderId = orderPaymentEventPayload.orderId,
                    outboxCallback = outboxCallback,
                )
            )
            logger.info(
                "OrderPaymentEventPayload sent to kafka for order id: ${orderPaymentEventPayload.orderId} and " +
                        "saga id: $sagaId"
            )
        } catch (e: Exception) {
            logger.error(
                "Error while sending OrderPaymentEventPayload to kafka with order id:" +
                        " ${orderPaymentEventPayload.orderId} and saga id: $sagaId, error: ${e.message}"
            )
        }
    }
}
