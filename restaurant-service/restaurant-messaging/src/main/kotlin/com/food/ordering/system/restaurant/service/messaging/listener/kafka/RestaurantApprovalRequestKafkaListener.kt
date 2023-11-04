package com.food.ordering.system.restaurant.service.messaging.listener.kafka

import com.food.ordering.system.kafka.consumer.KafkaConsumer
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel
import com.food.ordering.system.restaurant.service.domain.ports.input.message.listener.RestaurantApprovalRequestMessageListener
import com.food.ordering.system.restaurant.service.messaging.mapper.toRestaurantApproval
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class RestaurantApprovalRequestKafkaListener(
    private val restaurantApprovalRequestMessageListener: RestaurantApprovalRequestMessageListener,
) : KafkaConsumer<RestaurantApprovalRequestAvroModel> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(
        id = "\${kafka-consumer-config.restaurant-approval-consumer-group-id}",
        topics = ["\${restaurant-service.restaurant-approval-request-topic-name}]"],
    )
    override fun receive(
        @Payload messages: List<RestaurantApprovalRequestAvroModel>,
        @Header(KafkaHeaders.RECEIVED_KEY) keys: List<String>,
        @Header(KafkaHeaders.RECEIVED_PARTITION) partitions: List<Int>,
        @Header(KafkaHeaders.OFFSET) offsets: List<Long>,
    ) {
        logger.info("${messages.size} number of orders approval requests received with key ${keys.toString()}, " +
                "partitions ${partitions.toString()} and offsets: ${offsets.toString()}, sending for request approval")

        messages.forEach{restaurantApprovalRequestAvroModel ->
            restaurantApprovalRequestMessageListener
                .approveOrder(restaurantApprovalRequestAvroModel.toRestaurantApproval())
        }
    }
}
