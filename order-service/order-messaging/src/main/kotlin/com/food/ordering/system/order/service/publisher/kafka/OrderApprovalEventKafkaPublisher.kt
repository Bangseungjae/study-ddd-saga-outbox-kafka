package com.food.ordering.system.order.service.publisher.kafka

import com.food.ordering.system.kafka.config.producer.KafkaMessageHelper
import com.food.ordering.system.kafka.config.producer.service.KafkaProducer
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel
import com.food.ordering.system.order.service.domain.config.OrderServiceConfigData
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalEventPayload
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantapproval.RestaurantApprovalRequestMessagePublisher
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper
import com.food.ordering.system.outbox.OutboxStatus
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.function.BiConsumer

@Component
class OrderApprovalEventKafkaPublisher(
    private val orderMessagingDataMapper: OrderMessagingDataMapper,
    private val kafkaProducer: KafkaProducer<String, RestaurantApprovalRequestAvroModel>,
    private val orderServiceConfigData: OrderServiceConfigData,
    private val kafkaMessageHelper: KafkaMessageHelper,
) : RestaurantApprovalRequestMessagePublisher {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun publish(
        orderApprovalOutboxMessage: OrderApprovalOutboxMessage,
        outboxCallback: BiConsumer<OrderApprovalOutboxMessage, OutboxStatus>,
    ) {
        val orderApprovalEventPayload = kafkaMessageHelper.getOrderEventPayload(
            payload = orderApprovalOutboxMessage.payload,
            outputType = OrderApprovalEventPayload::class.java,
        )

        val sagaId = orderApprovalOutboxMessage.sagaId.toString()

        logger.info(
            "Received OrderApprovalOutboxMessage for order id: ${orderApprovalEventPayload.orderId} " +
                    "and saga id: $sagaId"
        )

        try {
            val restaurantApprovalRequestAvroModel =
                orderMessagingDataMapper.orderApprovalEventToRestaurantApprovalRequestAvroModel(
                    sagaId,
                    orderApprovalEventPayload,
                )

            kafkaProducer.send(
                topicName = orderServiceConfigData.restaurantApprovalRequestTopicName,
                key = sagaId,
                message = restaurantApprovalRequestAvroModel,
                callback = kafkaMessageHelper.getKafkaCallback(
                    responseTopicName = orderServiceConfigData.restaurantApprovalRequestTopicName,
                    avroModel = restaurantApprovalRequestAvroModel,
                    outboxMessage = orderApprovalOutboxMessage,
                    outboxCallback = outboxCallback,
                    orderId = orderApprovalEventPayload.orderId,
                    avroModelName = "RestaurantApprovalRequestAvroModel",
                )
            )

            logger.info(
                "OrderApprovalEventPayload sent to kafka for order id: " +
                        "${restaurantApprovalRequestAvroModel.orderId} and saga id: $sagaId"
            )
        } catch (e: Exception) {
            logger.error(
                "Error while sending OrderApprovalEventPayload to kafka for order id:" +
                        " ${orderApprovalEventPayload.orderId} and saga id: $sagaId, error: ${e.message}"
            )
        }
    }

}
