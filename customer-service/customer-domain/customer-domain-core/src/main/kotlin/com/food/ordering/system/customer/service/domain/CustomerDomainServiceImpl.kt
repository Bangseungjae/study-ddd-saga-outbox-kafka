package com.food.ordering.system.customer.service.domain

import com.food.ordering.system.customer.service.domain.entity.Customer
import com.food.ordering.system.customer.service.domain.event.CustomerCreatedEvent
import com.food.ordering.system.domain.KOREA_DATE_TIME
import org.slf4j.LoggerFactory
import java.time.ZoneId
import java.time.ZonedDateTime

class CustomerDomainServiceImpl : CustomerDomainService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun validateAndInitiateCustomer(customer: Customer): CustomerCreatedEvent {
        logger.info("Customer with id: ${customer.id.value} is initiated")
        return CustomerCreatedEvent(
            customer = customer,
            createdAt = ZonedDateTime.now(ZoneId.of(KOREA_DATE_TIME))
        )
    }
}
