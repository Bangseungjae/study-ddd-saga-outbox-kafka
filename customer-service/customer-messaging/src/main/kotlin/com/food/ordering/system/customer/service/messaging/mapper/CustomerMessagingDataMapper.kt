package com.food.ordering.system.customer.service.messaging.mapper

import com.food.ordering.system.customer.service.domain.event.CustomerCreatedEvent
import com.food.ordering.system.kafka.order.avro.model.CustomerAvroModel

fun CustomerCreatedEvent.toCustomerAvroModel(): CustomerAvroModel = run {
    CustomerAvroModel.newBuilder()
        .setId(customer.id.value)
        .setUsername(customer.username)
        .setFirstName(customer.firstName)
        .setLastName(customer.lastName)
        .build()
}
