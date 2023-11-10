package com.food.ordering.system.customer.service.domain

import com.food.ordering.system.customer.service.domain.entity.Customer
import com.food.ordering.system.customer.service.domain.event.CustomerCreatedEvent

interface CustomerDomainService {

    fun validateAndInitiateCustomer(customer: Customer): CustomerCreatedEvent
}
