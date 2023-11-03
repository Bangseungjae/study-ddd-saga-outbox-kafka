package com.food.ordering.system.kafka.config.producer.service

import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel
import org.apache.avro.specific.SpecificRecordBase
import org.springframework.kafka.support.SendResult
import org.springframework.util.concurrent.ListenableFutureCallback
import java.io.Serializable

interface KafkaProducer <K: Serializable, V: SpecificRecordBase> {
    fun send(
        topicName: String,
        key: K,
        message: V,
        callback: ListenableFutureCallback<SendResult<K, V>>,
    )
}
