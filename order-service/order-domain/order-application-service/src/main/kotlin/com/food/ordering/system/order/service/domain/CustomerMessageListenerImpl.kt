package com.food.ordering.system.order.service.domain

import com.food.ordering.system.order.service.domain.dto.message.CustomerModel
import com.food.ordering.system.order.service.domain.entity.Customer
import com.food.ordering.system.order.service.domain.exception.OrderDomainException
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.customer.CustomerMessageListener
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class CustomerMessageListenerImpl(
    private val customerRepository: CustomerRepository,
    private val orderDataMapper: OrderDataMapper,
) : CustomerMessageListener {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun customerCreated(customerModel: CustomerModel) {
        val customer: Customer
        try {
            customer = customerRepository.save(
                orderDataMapper.customerModelToCustomer(customerModel)
            )
        } catch (e: Exception) {
            logger.error("Customer could not be created in order database with id: ${customerModel.id}")
            throw OrderDomainException("Customer could not be created in order database with id: ${customerModel.id}")
        }
        logger.info("Customer is created in order database with id: ${customer.id}")
    }
}
