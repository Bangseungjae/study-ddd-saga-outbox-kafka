package com.food.ordering.system.order.service.dataaccess.customer.mapper

import com.food.ordering.system.domain.valueobject.CustomerId
import com.food.ordering.system.order.service.dataaccess.customer.entity.CustomerEntity
import com.food.ordering.system.order.service.domain.entity.Customer
import org.springframework.stereotype.Component

@Component
class CustomerDataAccessMapper {

    fun customerEntityToCustomer(customerEntity: CustomerEntity): Customer = Customer(
        customerId = CustomerId(customerEntity.id),
        username = customerEntity.username,
        firstName = customerEntity.firstName,
        lastName = customerEntity.lastName,
    )


    fun customerToCustomerEntity(customer: Customer): CustomerEntity = CustomerEntity(
        id = customer.id.value,
        username = customer.username,
        firstName = customer.firstName,
        lastName = customer.lastName,
    )
}
