package com.food.ordering.system.restaurant.service.messaging.publisher.kafka

import com.food.ordering.system.kafka.config.producer.KafkaMessageHelper
import com.food.ordering.system.kafka.config.producer.service.KafkaProducer
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel
import com.food.ordering.system.restaurant.service.domain.config.RestaurantServiceConfigData
import com.food.ordering.system.restaurant.service.domain.event.OrderRejectedEvent
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.OrderRejectedMessagePublisher
import com.food.ordering.system.restaurant.service.messaging.mapper.toRestaurantApprovalResponseAvroModel
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class OrderRejectedKafkaMessagingPublisher(
    private val kafkaProducer: KafkaProducer<String, RestaurantApprovalResponseAvroModel>,
    private val restaurantServiceConfigData: RestaurantServiceConfigData,
    private val kafkaMessageHelper: KafkaMessageHelper,
) : OrderRejectedMessagePublisher {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun publish(domainEvent: OrderRejectedEvent) {
        val orderId = domainEvent.orderApproval.orderId.value.toString()
        try {
            val restaurantApprovalResponseAvroModel = domainEvent.toRestaurantApprovalResponseAvroModel()

            kafkaProducer.send(
                topicName = restaurantServiceConfigData.restaurantApprovalResponseTopicName,
                key = orderId,
                message = restaurantApprovalResponseAvroModel,
                callback = kafkaMessageHelper.getKafkaCallback(
                    responseTopicName = restaurantServiceConfigData.restaurantApprovalResponseTopicName,
                    avroModel = restaurantApprovalResponseAvroModel,
                    orderId = orderId,
                    avroModelName = "RestaurantApprovalResponseAvroModel"

                )
            )
            logger.info("RestaurantApprovalResponseAvroModel sent to kafka at: ${System.nanoTime()}")
        } catch (e: Exception) {
            logger.error("Error while sending RestaurantApprovalResponseAvroModel message to " +
                    "kafka with order id: ${orderId}, error: ${e.message}")
        }

    }
}
