package com.food.ordering.system.payment.service.domain

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan

@ConfigurationPropertiesScan
@SpringBootApplication(
    scanBasePackages = ["com.food.ordering.system.payment.service.domain"],
//    scanBasePackageClasses = [PaymentServiceConfigData::class],
)
class PaymentDomainApplication {
}
