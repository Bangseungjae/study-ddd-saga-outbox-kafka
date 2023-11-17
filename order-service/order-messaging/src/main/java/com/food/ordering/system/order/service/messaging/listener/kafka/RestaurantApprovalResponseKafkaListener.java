package com.food.ordering.system.order.service.messaging.listener.kafka;

import com.food.ordering.system.domain.event.payload.RestaurantOrderEventPayload;
import com.food.ordering.system.kafka.config.producer.KafkaMessageHelper;
import com.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.OrderApprovalStatus;
import com.food.ordering.system.messaging.DebeziumOp;
import com.food.ordering.system.order.service.domain.exception.OrderNotFoundException;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import debezium.restaurant.order_outbox.Envelope;
import debezium.restaurant.order_outbox.Value;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component
public class RestaurantApprovalResponseKafkaListener implements KafkaConsumer<Envelope> {

    private final RestaurantApprovalResponseMessageListener restaurantApprovalResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final KafkaMessageHelper kafkaMessageHelper;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public RestaurantApprovalResponseKafkaListener(RestaurantApprovalResponseMessageListener restaurantApprovalResponseMessageListener,
                                                   OrderMessagingDataMapper orderMessagingDataMapper,
                                                   KafkaMessageHelper kafkaMessageHelper) {
        this.restaurantApprovalResponseMessageListener = restaurantApprovalResponseMessageListener;
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    @KafkaListener(
            id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}",
            topics = "${order-service.restaurant-approval-response-topic-name}"
    )
    public void receive(
            @Payload @NotNull List<? extends Envelope> messages,
            @Header(KafkaHeaders.RECEIVED_KEY) @NotNull List<String> keys,
            @Header(KafkaHeaders.RECEIVED_PARTITION) @NotNull List<Integer> partitions,
            @Header(KafkaHeaders.OFFSET) @NotNull List<Long> offsets
    ) {
        logger.info("{} number of restaurant approval responses received",
                messages.stream().filter(message ->
                                message.getBefore() == null && DebeziumOp.CREATE.getValue().equals(message.getOp()))
                        .toList().size());

        messages.forEach(avroModel -> {
            if (avroModel.getBefore() == null && DebeziumOp.CREATE.getValue().equals(avroModel.getOp())) {
                logger.info("Incoming message in RestaurantApprovalResponseKafkaListener: {}", avroModel);
                Value restaurantApprovalResponseAvroModel = avroModel.getAfter();
                RestaurantOrderEventPayload restaurantOrderEventPayload =
                        kafkaMessageHelper.getOrderEventPayload(restaurantApprovalResponseAvroModel.getPayload(), RestaurantOrderEventPayload.class);

                try {
                    if (OrderApprovalStatus.APPROVED.name().equals(restaurantOrderEventPayload.getOrderApprovalStatus())) {
                        logger.info("Processing approved order for order id: {}", restaurantOrderEventPayload.getOrderId());

                        restaurantApprovalResponseMessageListener.orderApproved(
                                orderMessagingDataMapper.approvalResponseAvroModelToApprovalResponse(
                                        restaurantOrderEventPayload,
                                        restaurantApprovalResponseAvroModel
                                )
                        );
                    } else if (OrderApprovalStatus.REJECTED.name().equals(restaurantOrderEventPayload.getOrderApprovalStatus())) {
                        logger.info("Processing rejected order for order id: {} with failure messages: {}",
                                restaurantOrderEventPayload.getOrderId(),
                                String.join(",", restaurantOrderEventPayload.getFailureMessages())
                        );
                        restaurantApprovalResponseMessageListener.orderRejected(
                                orderMessagingDataMapper.approvalResponseAvroModelToApprovalResponse(
                                        restaurantOrderEventPayload,
                                        restaurantApprovalResponseAvroModel
                                )
                        );
                    }
                } catch (OptimisticLockingFailureException e) {
                    /*
                     * NO-OP for optimistic lock. This means another thread finished the work, do not throw error
                     * to prevent reading the data from kafka again!
                     */
                    logger.error("Caught optimistic locking exception in " +
                                    "RestaurantApprovalResponseKafkaListener for order id: {}",
                            restaurantOrderEventPayload.getOrderId());
                } catch (OrderNotFoundException e) {
                    // NO-OP for OrderNotFoundException
                    logger.error("No order found for id: {}", restaurantOrderEventPayload.getOrderId());
                } catch (DataAccessException e) {
                    SQLException sqlException = (SQLException) e.getRootCause();
                    if (sqlException != null && sqlException.getSQLState() != null) {
                        logger.error("Caught unique constraint exception with sql state: {} in " +
                                        "RestaurantApprovalResponseKafkaListener for order id: {}",
                                sqlException.getSQLState(),
                                restaurantOrderEventPayload.getOrderId()
                        );
                    }
                }
            }
        });
    }
}
