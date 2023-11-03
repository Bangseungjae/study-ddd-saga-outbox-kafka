package com.food.ordering.system.payment.service.domain

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories(basePackages = ["com.food.ordering.system.payment.service.dataaccess"])
@EntityScan(basePackages = ["com.food.ordering.system.payment.service.dataaccess"])
@SpringBootApplication(scanBasePackages = ["com.food.ordering.system"])
class PaymentServiceApplication

fun main(args: Array<String>) {
    runApplication<PaymentServiceApplication>(*args)
}
