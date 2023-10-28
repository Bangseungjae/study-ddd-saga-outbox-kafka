package com.food.ordering.system.order.service.dataaccess.customer.mapper

import com.food.ordering.system.domain.valueobject.CustomerId
import com.food.ordering.system.order.service.dataaccess.customer.entity.CustomerEntity
import com.food.ordering.system.order.service.domain.entity.Customer
import org.springframework.stereotype.Component

@Component
class CustomerDataAccessMapper {

    fun customerEntityToCustomer(customerEntity: CustomerEntity?): Customer? =
        customerEntity?.let {
            Customer(customerId = CustomerId(it.id))
        }
}
