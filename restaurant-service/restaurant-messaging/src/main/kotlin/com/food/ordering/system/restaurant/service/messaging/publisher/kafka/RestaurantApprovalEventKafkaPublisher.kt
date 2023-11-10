package com.food.ordering.system.restaurant.service.messaging.publisher.kafka

import com.food.ordering.system.kafka.config.producer.KafkaMessageHelper
import com.food.ordering.system.kafka.config.producer.service.KafkaProducer
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel
import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.restaurant.service.domain.config.RestaurantServiceConfigData
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderEventPayload
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderOutboxMessage
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.RestaurantApprovalResponseMessagePublisher
import com.food.ordering.system.restaurant.service.messaging.mapper.toRestaurantApprovalResponseAvroModel
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.function.BiConsumer

@Component
class RestaurantApprovalEventKafkaPublisher(
    private val kafkaProducer: KafkaProducer<String, RestaurantApprovalResponseAvroModel>,
    private val restaurantServiceConfigData: RestaurantServiceConfigData,
    private val kafkaMessageHelper: KafkaMessageHelper,
) : RestaurantApprovalResponseMessagePublisher {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun publish(
        orderOutboxMessage: OrderOutboxMessage,
        outboxCallback: BiConsumer<OrderOutboxMessage, OutboxStatus>,
    ) {
        val orderEventPayload =
            kafkaMessageHelper.getOrderEventPayload(orderOutboxMessage.payload, OrderEventPayload::class.java)
        val sagaId = orderOutboxMessage.sagaId.toString()

        logger.info("Received OrderOutboxMessage for order id: ${orderEventPayload.orderId} and saga id: $sagaId")

        try {
            val restaurantApprovalResponseAvroModel =
                orderEventPayload.toRestaurantApprovalResponseAvroModel(sagaId)

            kafkaProducer.send(
                topicName = restaurantServiceConfigData.restaurantApprovalResponseTopicName,
                key = sagaId,
                message = restaurantApprovalResponseAvroModel,
                callback = kafkaMessageHelper.getKafkaCallback(
                    responseTopicName = restaurantServiceConfigData.restaurantApprovalResponseTopicName,
                    avroModel = restaurantApprovalResponseAvroModel,
                    avroModelName = "RestaurantApprovalResponseAvroModel",
                    outboxCallback = outboxCallback,
                    outboxMessage = orderOutboxMessage,
                    orderId = orderEventPayload.orderId,
                )
            )

            logger.info(
                "RestaurantApprovalResponseAvroModel sent to kafka for order id: " +
                        "${restaurantApprovalResponseAvroModel.orderId} and saga id: $sagaId"
            )
        } catch (e: Exception) {
            logger.error("Error while sending RestaurantApprovalResponseAvroModel message to kafka with order id: " +
                    "${orderEventPayload.orderId} and saga id: $sagaId, error: ${e.message}")
        }


    }
}
