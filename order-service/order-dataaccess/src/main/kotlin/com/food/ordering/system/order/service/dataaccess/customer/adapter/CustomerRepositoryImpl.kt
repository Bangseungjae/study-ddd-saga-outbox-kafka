package com.food.ordering.system.order.service.dataaccess.customer.adapter

import com.food.ordering.system.order.service.dataaccess.customer.mapper.CustomerDataAccessMapper
import com.food.ordering.system.order.service.dataaccess.customer.repository.CustomerJpaRepository
import com.food.ordering.system.order.service.domain.entity.Customer
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException
import java.util.*

@Component
class CustomerRepositoryImpl(
    private val customerJpaRepository: CustomerJpaRepository,
    private val customerDataAccessMapper: CustomerDataAccessMapper,
) : CustomerRepository {
    override fun findCustomer(customerId: UUID): Customer? {
        val customerEntity = customerJpaRepository.findById(customerId)
            .orElseThrow { IllegalArgumentException("customer not found id: $customerId") }
        return customerDataAccessMapper.customerEntityToCustomer(customerEntity)
    }

    override fun save(customer: Customer): Customer {
        return customerDataAccessMapper.customerEntityToCustomer(
            customerJpaRepository.save(
                customerDataAccessMapper.customerToCustomerEntity(customer)
            )
        )
    }
}
