package com.food.ordering.system.restaurant.service.messaging.publisher.kafka

import com.food.ordering.system.kafka.config.producer.KafkaMessageHelper
import com.food.ordering.system.kafka.config.producer.service.KafkaProducer
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel
import com.food.ordering.system.restaurant.service.domain.config.RestaurantServiceConfigData
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovedEvent
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.OrderApprovedMessagePublisher
import com.food.ordering.system.restaurant.service.messaging.mapper.toRestaurantApprovalResponseAvroModel
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class OrderApprovedKafkaMessagePublisher(
    private val kafkaProducer: KafkaProducer<String, RestaurantApprovalResponseAvroModel>,
    private val restaurantServiceConfigData: RestaurantServiceConfigData,
    private val kafkaMessageHelper: KafkaMessageHelper,
) : OrderApprovedMessagePublisher {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun publish(domainEvent: OrderApprovedEvent) {
        val orderId = domainEvent.orderApproval.orderId.value.toString()

        logger.info("Received OrderApprovedEvent for order id: ${orderId}")

        try {
            val restaurantApprovalResponseAvroModel = domainEvent.toRestaurantApprovalResponseAvroModel()
            kafkaProducer.send(
                topicName = restaurantServiceConfigData.restaurantApprovalResponseTopicName,
                key = orderId,
                message = restaurantApprovalResponseAvroModel,
                callback = kafkaMessageHelper.getKafkaCallback(
                    paymentResponseTopicName = restaurantServiceConfigData.restaurantApprovalResponseTopicName,
                    avroModel = restaurantApprovalResponseAvroModel,
                    orderId = orderId,
                    avroModelName = "RestaurantApprovalResponseAvroModel"
                )
            )

            logger.info("RestaurantApprovalResponseAvroModel sent to kafka at: ${System.nanoTime()}")
        } catch (e: Exception) {
            logger.error("Error while sending RestaurantApprovalResponseAvroModel message to kafka" +
                    " with order id: ${orderId}, error: ${e.message}")
        }
    }
}
