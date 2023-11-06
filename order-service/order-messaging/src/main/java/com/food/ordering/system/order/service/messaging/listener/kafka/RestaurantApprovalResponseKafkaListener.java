package com.food.ordering.system.order.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.OrderApprovalStatus;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RestaurantApprovalResponseKafkaListener implements KafkaConsumer<RestaurantApprovalResponseAvroModel> {

    private final RestaurantApprovalResponseMessageListener restaurantApprovalResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public RestaurantApprovalResponseKafkaListener(RestaurantApprovalResponseMessageListener restaurantApprovalResponseMessageListener,
                                                   OrderMessagingDataMapper orderMessagingDataMapper
    ) {
        this.restaurantApprovalResponseMessageListener = restaurantApprovalResponseMessageListener;
        this.orderMessagingDataMapper = orderMessagingDataMapper;
    }

    @Override
    @KafkaListener(
            id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}",
            topics = "${order-service.restaurant-approval-response-topic-name}"
    )
    public void receive(
            @Payload @NotNull List<? extends RestaurantApprovalResponseAvroModel> messages,
            @Header(KafkaHeaders.RECEIVED_KEY) @NotNull List<String> keys,
            @Header(KafkaHeaders.RECEIVED_PARTITION) @NotNull List<Integer> partitions,
            @Header(KafkaHeaders.OFFSET) @NotNull List<Long> offsets
    ) {
        logger.info("{} number of restaurant approval responses received with keys {}, partitions: {} and offsets: {}",
                messages.size(),
                keys,
                partitions,
                offsets
        );

        messages.forEach(restaurantApprovalResponseAvroModel -> {
            logger.info("failure messages: {}", restaurantApprovalResponseAvroModel.getFailureMessages());
            if (OrderApprovalStatus.APPROVED == restaurantApprovalResponseAvroModel.getOrderApprovalStatus()) {
                logger.info("Processing approved order for order id: {}", restaurantApprovalResponseAvroModel.getOrderId());

                restaurantApprovalResponseMessageListener.orderApproved(
                        orderMessagingDataMapper.approvalResponseAvroModelToApprovalResponse(restaurantApprovalResponseAvroModel)
                );
            } else if (OrderApprovalStatus.REJECTED == restaurantApprovalResponseAvroModel.getOrderApprovalStatus()) {
                logger.info("reject failure message: {}", restaurantApprovalResponseAvroModel.getFailureMessages());
                logger.info("Processing rejected order for order id: {} with failure messages: {}",
                        restaurantApprovalResponseAvroModel.getOrderId(),
                        String.join(",", restaurantApprovalResponseAvroModel.getFailureMessages())
                );

                restaurantApprovalResponseMessageListener.orderRejected(
                        orderMessagingDataMapper.approvalResponseAvroModelToApprovalResponse(restaurantApprovalResponseAvroModel)
                );
            }
        });
    }
}
