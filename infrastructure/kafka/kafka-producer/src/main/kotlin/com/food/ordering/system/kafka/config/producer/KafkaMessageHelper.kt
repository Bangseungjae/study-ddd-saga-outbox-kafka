package com.food.ordering.system.kafka.config.producer

import org.slf4j.LoggerFactory
import org.springframework.kafka.support.SendResult
import org.springframework.lang.Nullable
import org.springframework.stereotype.Component
import org.springframework.util.concurrent.ListenableFutureCallback

@Component
class KafkaMessageHelper {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun <T> getKafkaCallback(
        paymentResponseTopicName: String,
        avroModel: T,
        orderId: String,
        avroModelName: String,
    ): ListenableFutureCallback<SendResult<String, T>> {
        return object : ListenableFutureCallback<SendResult<String, T>> {
            override fun onSuccess(@Nullable result: SendResult<String, T>?) {
                val metadata = result?.recordMetadata
                if (metadata != null) {
                    logger.info(
                        "Received successful response from Kafka for order id: {}" +
                                "Topic: {} Partition: {} offset: {} Timestamp: {}",
                        orderId,
                        metadata.topic(),
                        metadata.offset(),
                        metadata.timestamp()
                    )
                }
            }

            override fun onFailure(ex: Throwable) {
                logger.error("Error while sending $avroModelName to topic {}", paymentResponseTopicName, ex)
            }
        }

    }
}
