package com.food.ordering.system.customer.service.domain

import com.food.ordering.system.customer.service.domain.create.CreateCustomerCommand
import com.food.ordering.system.customer.service.domain.create.CreateCustomerResponse
import com.food.ordering.system.customer.service.domain.mapper.toCreateCustomerCommand
import com.food.ordering.system.customer.service.domain.ports.input.service.CustomerApplicationService
import com.food.ordering.system.customer.service.domain.ports.output.message.publisher.CustomerMessagePublisher
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CustomerApplicationServiceImpl(
    private val customerCreateCommandHandler: CustomerCreateCommandHandler,
    private val customerMessagePublisher: CustomerMessagePublisher,
) : CustomerApplicationService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun createCustomer(createCustomerCommand: CreateCustomerCommand): CreateCustomerResponse {
        val customerCreatedEvent = customerCreateCommandHandler.createCustomer(createCustomerCommand)
        customerMessagePublisher.publish(customerCreatedEvent)
        return customerCreatedEvent
            .customer
            .toCreateCustomerCommand("Customer saved successfully!")
    }
}
