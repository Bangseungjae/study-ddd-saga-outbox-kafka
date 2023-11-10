package com.food.ordering.system.customer.service.messaging.publisher.kafka

import com.food.ordering.system.customer.service.domain.config.CustomerServiceConfigData
import com.food.ordering.system.customer.service.domain.event.CustomerCreatedEvent
import com.food.ordering.system.customer.service.domain.ports.output.message.publisher.CustomerMessagePublisher
import com.food.ordering.system.customer.service.messaging.mapper.toCustomerAvroModel
import com.food.ordering.system.kafka.config.producer.service.KafkaProducer
import com.food.ordering.system.kafka.order.avro.model.CustomerAvroModel
import org.slf4j.LoggerFactory
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import java.util.function.BiConsumer

@Component
class CustomerCreatedEventKafkaPublisher(
    private val kafkaProducer: KafkaProducer<String, CustomerAvroModel>,
    private val customerServiceConfigData: CustomerServiceConfigData,
) : CustomerMessagePublisher {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun publish(customerCreatedEvent: CustomerCreatedEvent) {
        try {
            val customerAvroModel = customerCreatedEvent.toCustomerAvroModel()

            kafkaProducer.send(
                topicName = customerServiceConfigData.customerTopicName,
                key = customerAvroModel.id.toString(),
                message = customerAvroModel,
                callback = getCallback(
                    topicName = customerServiceConfigData.customerTopicName,
                    message = customerAvroModel,
                )
            )
        } catch (e: Exception) {
            logger.error("Error while sending CustomerCreatedEvent to kafka for customer id: " +
                    "${customerCreatedEvent.customer.id.value} error: ${e.message}")
        }
    }

    private fun getCallback(
        topicName: String,
        message: CustomerAvroModel,
    ): BiConsumer<SendResult<String, CustomerAvroModel>, Throwable?> {
        return BiConsumer { result, ex ->
            ex?.run {
                val metadata = result.recordMetadata
                logger.info("Received new metadata Topic: ${metadata.topic()}; Partition: ${metadata.partition()}; " +
                        "Offset: ${metadata.offset()}; Timestamp: ${metadata.timestamp()}; at time: ${System.nanoTime()}")
            } ?: run {
                logger.error("Error while sending message $message to topic $topicName")
            }
        }
    }
}

