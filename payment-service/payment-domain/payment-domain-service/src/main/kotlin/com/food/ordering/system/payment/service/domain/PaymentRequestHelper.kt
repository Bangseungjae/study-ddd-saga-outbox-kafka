package com.food.ordering.system.payment.service.domain

import com.food.ordering.system.domain.valueobject.CustomerId
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest
import com.food.ordering.system.payment.service.domain.entity.CreditEntry
import com.food.ordering.system.payment.service.domain.entity.CreditHistory
import com.food.ordering.system.payment.service.domain.entity.Payment
import com.food.ordering.system.payment.service.domain.event.PaymentEvent
import com.food.ordering.system.payment.service.domain.exception.PaymentApplicationServiceException
import com.food.ordering.system.payment.service.domain.mapper.PaymentDataMapper
import com.food.ordering.system.payment.service.domain.ports.output.output.repostiroy.CreditEntryRepository
import com.food.ordering.system.payment.service.domain.ports.output.output.repostiroy.CreditHistoryRepository
import com.food.ordering.system.payment.service.domain.ports.output.output.repostiroy.PaymentRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Component
class PaymentRequestHelper(
    private val paymentDomainService: PaymentDomainService,
    private val paymentDataMapper: PaymentDataMapper,
    private val paymentRepository: PaymentRepository,
    private val creditEntryRepository: CreditEntryRepository,
    private val creditHistoryRepository: CreditHistoryRepository,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun persistPayment(paymentRequest: PaymentRequest): PaymentEvent {
        logger.info("Received payment complete event for order id: ${paymentRequest.orderId}")
        val payment = paymentDataMapper.paymentRequestModelToPayment(paymentRequest)
        val creditEntry: CreditEntry =  getCreditEntry(CustomerId(UUID.fromString(paymentRequest.customerId)))
        val creditHistories: MutableList<CreditHistory> = getCreditHistory(payment.customerId)
        val failureMessages = mutableListOf<String>()
        val paymentEvent: PaymentEvent = paymentDomainService.validateAndInitiatePayment(
            payment = payment,
            creditEntry = creditEntry,
            creditHistories = creditHistories,
            failureMessages = failureMessages,
        )
        persistDbObjects(
            payment = payment,
            failureMessages = failureMessages,
            creditEntry = creditEntry,
            creditHistories = creditHistories,
        )
        return paymentEvent
    }

    private fun persistDbObjects(
        payment: Payment,
        failureMessages: MutableList<String>,
        creditEntry: CreditEntry,
        creditHistories: MutableList<CreditHistory>,
    ) {
        paymentRepository.save(payment)
        if (failureMessages.isEmpty()) {
            creditEntryRepository.save(creditEntry)
            creditHistoryRepository.save(creditHistories[creditHistories.size - 1])
        }
    }

    @Transactional
    fun persistCancelPayment(paymentRequest: PaymentRequest): PaymentEvent {
        logger.info("Received payment rollback event for order id: ${paymentRequest.orderId}")
        val payment = paymentRepository.findByOrderId(UUID.fromString(paymentRequest.orderId)) ?: run {
            logger.error("Payment with order id: ${paymentRequest.orderId} could not be find!")
            throw PaymentApplicationServiceException("Payment with order id: ${paymentRequest.orderId} could not be found!")
        }
        val creditEntry = getCreditEntry(payment.customerId)
        val creditHistories = getCreditHistory(payment.customerId)
        val failureMessages = mutableListOf<String>()
        val paymentEvent = paymentDomainService.validateAndCancelPayment(
            payment = payment,
            creditEntry = creditEntry,
            creditHistories = creditHistories,
            failureMessages = failureMessages,
        )
        persistDbObjects(
            payment = payment,
            failureMessages = failureMessages,
            creditEntry = creditEntry,
            creditHistories = creditHistories,
        )
        return paymentEvent
    }


    private fun getCreditHistory(customerId: CustomerId): MutableList<CreditHistory> {
        val creditHistories = creditHistoryRepository.findByCustomerId(customerId)
        if (creditHistories.isEmpty()) {
            logger.error("Could not find credit history for customer: ${customerId.value}")
            throw PaymentApplicationServiceException("Could not find credit history for customer: ${customerId.value}")
        }
        return creditHistories
    }

    private fun getCreditEntry(customerId: CustomerId): CreditEntry {
        return creditEntryRepository.findByCustomerId(customerId)?:run {
            logger.error("Could not find credit entry for customer: ${customerId.value}")
            throw PaymentApplicationServiceException("Could not find credit entry for customer: ${customerId.value}")
        }
    }
}
