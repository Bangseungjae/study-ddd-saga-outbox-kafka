package com.food.ordering.system.kafka.config.producer

import com.food.ordering.system.kafka.config.data.KafkaConfigData
import com.food.ordering.system.kafka.config.data.KafkaProducerConfigData
import org.apache.avro.specific.SpecificRecordBase
import org.apache.kafka.clients.producer.ProducerConfig
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
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigData.bootstrapServers)
        props.put(kafkaConfigData.schemaRegistryUrlKey, kafkaConfigData.schemaRegistryUrl)
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProducerConfigData.keySerializerClass)
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProducerConfigData.valueSerializerClass)
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProducerConfigData.batchSize * kafkaProducerConfigData.batchSizeBoostFactor)
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerConfigData.lingerMs)
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, kafkaProducerConfigData.compressionType)
        props.put(ProducerConfig.ACKS_CONFIG, kafkaProducerConfigData.acks)
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaProducerConfigData.requestTimeoutMs)
        props.put(ProducerConfig.RETRIES_CONFIG, kafkaProducerConfigData.retryCount)
        return props
    }

    @Bean
    fun producerFactory(): ProducerFactory<K, V> {
        return DefaultKafkaProducerFactory(producerConfig())
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<K, V> = KafkaTemplate(producerFactory())

}
