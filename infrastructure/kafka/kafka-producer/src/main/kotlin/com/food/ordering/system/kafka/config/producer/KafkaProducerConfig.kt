package com.food.ordering.system.kafka.config.producer

import com.food.ordering.system.kafka.config.data.KafkaConfigData
import com.food.ordering.system.kafka.config.data.KafkaProducerConfigData
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.avro.specific.SpecificRecordBase
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import java.io.Serializable

@Configuration
class KafkaProducerConfig <K: Serializable, V: SpecificRecordBase>(
    private val kafkaConfigData: KafkaConfigData,
    private val kafkaProducerConfigData: KafkaProducerConfigData,
) {

    @Bean
    fun producerConfig(): MutableMap<String, Any> {

        val props = mutableMapOf<String, Any>()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaConfigData.bootstrapServers
        props[kafkaConfigData.schemaRegistryUrlKey] = kafkaConfigData.schemaRegistryUrl
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = KafkaAvroSerializer::class.java
        props[ProducerConfig.BATCH_SIZE_CONFIG] = kafkaProducerConfigData.batchSize * kafkaProducerConfigData.batchSizeBoostFactor
        props[ProducerConfig.LINGER_MS_CONFIG] = kafkaProducerConfigData.lingerMs
        props[ProducerConfig.COMPRESSION_TYPE_CONFIG] = kafkaProducerConfigData.compressionType
        props[ProducerConfig.ACKS_CONFIG] = kafkaProducerConfigData.acks
        props[ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG] = kafkaProducerConfigData.requestTimeoutMs
        props[ProducerConfig.RETRIES_CONFIG] = kafkaProducerConfigData.retryCount
        return props
    }

    @Bean
    fun producerFactory(): ProducerFactory<K, V> {
        return DefaultKafkaProducerFactory(producerConfig())
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<K, V> = KafkaTemplate(producerFactory())

}
