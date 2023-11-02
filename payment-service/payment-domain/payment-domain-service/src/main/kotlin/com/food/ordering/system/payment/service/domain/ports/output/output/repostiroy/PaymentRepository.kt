package com.food.ordering.system.payment.service.domain.ports.output.output.repostiroy

import com.food.ordering.system.payment.service.domain.entity.Payment
import java.util.UUID

interface PaymentRepository {
    fun save(payment: Payment): Payment

    fun findByOrderId(orderId: UUID): Payment?
}
