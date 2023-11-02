package com.food.ordering.system.payment.service.dataaccess.payment.repository

import com.food.ordering.system.payment.service.dataaccess.payment.entity.PaymentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PaymentJpaRepository : JpaRepository<PaymentEntity, UUID> {

    fun findByOrderId(orderId: UUID): PaymentEntity?
}
