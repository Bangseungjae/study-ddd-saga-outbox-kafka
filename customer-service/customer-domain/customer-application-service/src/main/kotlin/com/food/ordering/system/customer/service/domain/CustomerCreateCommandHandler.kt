package com.food.ordering.system.customer.service.domain

import com.food.ordering.system.customer.service.domain.create.CreateCustomerCommand
import com.food.ordering.system.customer.service.domain.event.CustomerCreatedEvent
import com.food.ordering.system.customer.service.domain.exception.CustomerDomainException
import com.food.ordering.system.customer.service.domain.mapper.toCustomer
import com.food.ordering.system.customer.service.domain.ports.output.repository.CustomerRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CustomerCreateCommandHandler(
    private val customerDomainService: CustomerDomainService,
    private val customerRepository: CustomerRepository,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun createCustomer(createCustomerCommand: CreateCustomerCommand): CustomerCreatedEvent {
        val customer = createCustomerCommand.toCustomer()
        val customerCreatedEvent = customerDomainService.validateAndInitiateCustomer(customer)
        try {
            customerRepository.createCustomer(customer)
        } catch (e: Exception) {
            logger.error("Could not save customer with id: ${createCustomerCommand.customerId}")
            throw CustomerDomainException("Could not save customer with id: ${createCustomerCommand.customerId}")
        }
        logger.info("Returning CustomerCreatedEvent for customer id: ${createCustomerCommand.customerId}")
        return customerCreatedEvent
    }
}
