package com.food.ordering.system.kafka.config.producer.exception

class KafkaProducerException(
    message: String = ""
) : RuntimeException(message)
