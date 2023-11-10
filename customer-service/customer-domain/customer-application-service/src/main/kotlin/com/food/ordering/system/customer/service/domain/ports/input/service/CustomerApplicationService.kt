package com.food.ordering.system.customer.service.domain.ports.input.service

import com.food.ordering.system.customer.service.domain.create.CreateCustomerCommand
import com.food.ordering.system.customer.service.domain.create.CreateCustomerResponse

interface CustomerApplicationService {

    fun createCustomer(createCustomerCommand: CreateCustomerCommand): CreateCustomerResponse
}
