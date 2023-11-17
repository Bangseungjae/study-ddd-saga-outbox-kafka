package com.food.ordering.system.restaurant.service.messaging.listener.kafka

import com.food.ordering.system.domain.event.payload.OrderApprovalEventPayload
import com.food.ordering.system.kafka.config.producer.KafkaMessageHelper
import com.food.ordering.system.kafka.consumer.KafkaConsumer
import com.food.ordering.system.messaging.DebeziumOp
import com.food.ordering.system.restaurant.service.domain.exception.RestaurantNotFoundException
import com.food.ordering.system.restaurant.service.domain.ports.input.message.listener.RestaurantApprovalRequestMessageListener
import com.food.ordering.system.restaurant.service.messaging.mapper.toRestaurantApproval
import debezium.order.restaurant_approval_outbox.Envelope
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class RestaurantApprovalRequestKafkaListener(
    private val restaurantApprovalRequestMessageListener: RestaurantApprovalRequestMessageListener,
    private val kafkaMessageHelper: KafkaMessageHelper,
) : KafkaConsumer<Envelope> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(
        id = "\${kafka-consumer-config.restaurant-approval-consumer-group-id}",
        topics = ["\${restaurant-service.restaurant-approval-request-topic-name}"],
    )
    override fun receive(
        @Payload messages: List<Envelope>,
        @Header(KafkaHeaders.RECEIVED_KEY) keys: List<String>,
        @Header(KafkaHeaders.RECEIVED_PARTITION) partitions: List<Int>,
        @Header(KafkaHeaders.OFFSET) offsets: List<Long>,
    ) {
        logger.info(
            "${messages.filter { it.before == null && DebeziumOp.CREATE.value == it.op }.size} number of " +
                    "restaurant approval requests received!"
        )

        messages.forEach { avroModel ->
            if (avroModel.before == null && DebeziumOp.CREATE.value == avroModel.op) {
                val restaurantApprovalRequestAvroModel = avroModel.after
                val orderApprovalEventPayload = kafkaMessageHelper.getOrderEventPayload(
                    restaurantApprovalRequestAvroModel.payload,
                    OrderApprovalEventPayload::class.java
                )
                try {
                    logger.info("Processing order approval for order id: ${orderApprovalEventPayload.orderId}")
                    restaurantApprovalRequestMessageListener
                        .approveOrder(orderApprovalEventPayload.toRestaurantApproval(restaurantApprovalRequestAvroModel))
                } catch (e: DataAccessException) {
                    //NO-OP for unique constraint exception
                    logger.error(
                        "Caught unique constraint exception with sql state: ${e.rootCause} " +
                                "in RestaurantApprovalRequestKafkaListener for order id: ${orderApprovalEventPayload.orderId}"
                    )
                } catch (e: RestaurantNotFoundException) {
                    //NO-OP for RestaurantNotFoundException
                    logger.error(
                        "No restaurant found for restaurant id: ${orderApprovalEventPayload.restaurantId}, " +
                                "and order id: ${orderApprovalEventPayload.orderId}"
                    )
                }
            }
        }
    }
}
