package com.food.ordering.system.order.service.domain.ports.output.message.listener.customer

import com.food.ordering.system.order.service.domain.dto.message.CustomerModel

interface CustomerMessageListener {

    fun customerCreated(customerModel: CustomerModel)
}
