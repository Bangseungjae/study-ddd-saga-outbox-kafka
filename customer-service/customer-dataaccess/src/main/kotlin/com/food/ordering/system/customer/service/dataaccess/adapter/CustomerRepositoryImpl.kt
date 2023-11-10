package com.food.ordering.system.customer.service.dataaccess.adapter

import com.food.ordering.system.customer.service.dataaccess.mapper.toCustomer
import com.food.ordering.system.customer.service.dataaccess.mapper.toCustomerEntity
import com.food.ordering.system.customer.service.dataaccess.repository.CustomerJpaRepository
import com.food.ordering.system.customer.service.domain.entity.Customer
import com.food.ordering.system.customer.service.domain.ports.output.repository.CustomerRepository
import org.springframework.stereotype.Component

@Component
class CustomerRepositoryImpl(
    private val customerJpaRepository: CustomerJpaRepository,
) : CustomerRepository {

    override fun createCustomer(customer: Customer): Customer {
        return customerJpaRepository.save(customer.toCustomerEntity())
            .toCustomer()
    }
}
