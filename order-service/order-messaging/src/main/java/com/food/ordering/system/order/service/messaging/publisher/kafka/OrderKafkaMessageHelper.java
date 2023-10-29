package com.food.ordering.system.order.service.messaging.publisher.kafka;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class OrderKafkaMessageHelper {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public <T> ListenableFutureCallback<SendResult<String, T>> getKafkaCallback(
            String paymentResponseTopicName,
            T requestAvroModel,
            String orderId,
            String requestAvroModelName
    ) {
        return new ListenableFutureCallback<SendResult<String, T>>() {

            @Override
            public void onSuccess(SendResult<String, T> result) {
                RecordMetadata metadata = result.getRecordMetadata();
                logger.info("Received successful response from Kafka for order id: {}" +
                                "Topic: {} Partition: {} offset: {} Timestamp: {}",
                        orderId,
                        metadata.topic(),
                        metadata.offset(),
                        metadata.timestamp());
            }

            @Override
            public void onFailure(Throwable ex) {
                logger.error("Error while sending "+ requestAvroModelName + " to topic {}", paymentResponseTopicName, ex);
            }
        };
    }
}
