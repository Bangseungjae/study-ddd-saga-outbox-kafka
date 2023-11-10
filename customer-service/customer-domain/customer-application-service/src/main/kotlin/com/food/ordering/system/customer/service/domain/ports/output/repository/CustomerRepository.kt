package com.food.ordering.system.customer.service.domain.ports.output.repository

import com.food.ordering.system.customer.service.domain.entity.Customer

interface CustomerRepository {

    fun createCustomer(customer: Customer): Customer
}
