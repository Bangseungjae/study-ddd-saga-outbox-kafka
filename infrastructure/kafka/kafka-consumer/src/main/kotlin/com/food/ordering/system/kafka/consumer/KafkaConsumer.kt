package com.food.ordering.system.kafka.consumer

import org.apache.avro.specific.SpecificRecordBase

interface KafkaConsumer <T: SpecificRecordBase> {
    fun receive(
        messages: List<T>,
        keys: List<Long>,
        partitions: List<Int>,
        offsets: List<Long>
    )
}
