package com.food.ordering.system.order.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.CustomerAvroModel;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.customer.CustomerMessageListener;
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
public class CustomerKafkaListener implements KafkaConsumer<CustomerAvroModel> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CustomerMessageListener customerMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    public CustomerKafkaListener(
            CustomerMessageListener customerMessageListener,
            OrderMessagingDataMapper orderMessagingDataMapper
    ) {
        this.customerMessageListener = customerMessageListener;
        this.orderMessagingDataMapper = orderMessagingDataMapper;
    }

    @Override
    @KafkaListener(
            id = "${kafka-consumer-config.customer-group-id}",
            topics = "${order-service.customer-topic-name}"
    )
    public void receive(
            @Payload @NotNull List<? extends CustomerAvroModel> messages,
            @Header(KafkaHeaders.RECEIVED_KEY) @NotNull List<String> keys,
            @Header(KafkaHeaders.RECEIVED_PARTITION) @NotNull List<Integer> partitions,
            @Header(KafkaHeaders.OFFSET) @NotNull List<Long> offsets
    ) {
        logger.info("{} number of customer create messages received with keys {}, partitions {} and offsets {}",
                messages.size(),
                keys,
                partitions,
                offsets);

        messages.forEach(customerAvroModel ->
                customerMessageListener.customerCreated(orderMessagingDataMapper
                        .customerAvroModelToCustomerModel(customerAvroModel)));
    }
}
