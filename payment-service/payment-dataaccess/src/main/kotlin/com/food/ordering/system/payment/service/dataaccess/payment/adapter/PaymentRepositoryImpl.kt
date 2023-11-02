package com.food.ordering.system.payment.service.dataaccess.payment.adapter

import com.food.ordering.system.payment.service.dataaccess.payment.entity.PaymentEntity
import com.food.ordering.system.payment.service.dataaccess.payment.mapper.PaymentDataAccessMapper
import com.food.ordering.system.payment.service.dataaccess.payment.repository.PaymentJpaRepository
import com.food.ordering.system.payment.service.domain.entity.Payment
import com.food.ordering.system.payment.service.domain.ports.output.output.repostiroy.PaymentRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class PaymentRepositoryImpl(
    private val paymentJpaRepository: PaymentJpaRepository,
    private val paymentDataAccessMapper: PaymentDataAccessMapper,
) : PaymentRepository {

    override fun save(payment: Payment): Payment = paymentDataAccessMapper.paymentEntityToPayment(
        paymentJpaRepository.save(paymentDataAccessMapper.paymentToPaymentEntity(payment))
    )

    override fun findByOrderId(orderId: UUID): Payment? {
        return paymentJpaRepository.findByOrderId(orderId)
            ?.let { paymentDataAccessMapper.paymentEntityToPayment(it) }
    }
}
