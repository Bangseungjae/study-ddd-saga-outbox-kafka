package com.food.ordering.system.kafka.consumer

import org.apache.avro.specific.SpecificRecordBase

interface KafkaSingleItemConsumer <T: SpecificRecordBase> {
    fun receive(
        message: T,
        key: String,
        partition: Int,
        offset: Long
    )
}
