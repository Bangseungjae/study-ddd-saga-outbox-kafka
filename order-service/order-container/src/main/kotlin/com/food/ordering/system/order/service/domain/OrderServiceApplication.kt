package com.food.ordering.system.order.service.domain

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication


@EntityScan(basePackages = ["com.food.ordering.system.order.service.dataaccess"])
@SpringBootApplication(scanBasePackages = ["com.food.ordering.system"])
class OrderServiceApplication {
}

fun main(args: Array<String>) {
    runApplication<OrderServiceApplication>(*args)
}
