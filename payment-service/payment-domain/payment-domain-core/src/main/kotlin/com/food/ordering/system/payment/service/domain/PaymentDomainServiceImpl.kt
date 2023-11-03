package com.food.ordering.system.payment.service.domain

import com.food.ordering.system.domain.KOREA_DATE_TIME
import com.food.ordering.system.domain.valueobject.Money
import com.food.ordering.system.domain.valueobject.PaymentStatus
import com.food.ordering.system.payment.service.domain.entity.CreditEntry
import com.food.ordering.system.payment.service.domain.entity.CreditHistory
import com.food.ordering.system.payment.service.domain.entity.Payment
import com.food.ordering.system.payment.service.domain.event.PaymentCancelledEvent
import com.food.ordering.system.payment.service.domain.event.PaymentCompletedEvent
import com.food.ordering.system.payment.service.domain.event.PaymentEvent
import com.food.ordering.system.payment.service.domain.event.PaymentFailedEvent
import com.food.ordering.system.payment.service.domain.valueobject.CreditHistoryId
import com.food.ordering.system.payment.service.domain.valueobject.TransactionType
import org.slf4j.LoggerFactory
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

class PaymentDomainServiceImpl : PaymentDomainService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun validateAndInitiatePayment(
        payment: Payment,
        creditEntry: CreditEntry,
        creditHistories: MutableList<CreditHistory>,
        failureMessages: MutableList<String>,
    ): PaymentEvent {
        payment.validatePayment(failureMessages)
        validateCreditEntry(payment, creditEntry, failureMessages)
        subtractCreditEntry(payment, creditEntry)
        updateCreditHistory(payment, creditHistories, TransactionType.DEBIT)
        validateCreditHistory(creditEntry, creditHistories, failureMessages)

        return when {
            failureMessages.isEmpty() -> {
                logger.info("Payment is initiated for order id: ${payment.orderId.id}")
                payment.updateStatus(PaymentStatus.COMPLETED)
                PaymentCompletedEvent(payment, ZonedDateTime.now(ZoneId.of(KOREA_DATE_TIME)))
            }

            else -> {
                logger.info("Payment initiation is failed for order id: ${payment.orderId.id}")
                PaymentFailedEvent(
                    payment = payment,
                    createdAt = ZonedDateTime.now(ZoneId.of(KOREA_DATE_TIME)),
                    failureMessages = failureMessages,
                )
            }
        }
    }

    private fun validateCreditHistory(
        creditEntry: CreditEntry,
        creditHistories: MutableList<CreditHistory>,
        failureMessages: MutableList<String>,
    ) {
        val totalCreditHistory = getTotalHistoryAmount(creditHistories, TransactionType.CREDIT)

        val totalDebitHistory = getTotalHistoryAmount(creditHistories, TransactionType.DEBIT)

        if (totalDebitHistory.isGreaterThan(totalCreditHistory)) {
            logger.error("Customer with id: ${creditEntry.customerId.value} doesn't have enough credit according to credit history")
            failureMessages.add("Customer with id=${creditEntry.customerId.value} doesn't have enough credit according to credit history!")
        }

        if (creditEntry.totalCreditAmount == totalCreditHistory.subtract(totalDebitHistory)) {
            logger.error("Credit history total is not equal to current credit for customer id: ${creditEntry.customerId.value}")
            failureMessages.add("Credit history total is not equal to current credit for customer id: ${creditEntry.customerId.value}!")
        }
    }

    private fun getTotalHistoryAmount(
        creditHistories: MutableList<CreditHistory>,
        transactionType: TransactionType,
    ): Money {
        return creditHistories.stream()
            .filter { creditHistory -> transactionType == creditHistory.transactionType }
            .map(CreditHistory::amount)
            .reduce(Money.ZERO, Money::add)
    }

    private fun updateCreditHistory(
        payment: Payment,
        creditHistories: MutableList<CreditHistory>,
        transactionType: TransactionType,
    ) {
        creditHistories.add(
            CreditHistory(
                creditHistoryId = CreditHistoryId(UUID.randomUUID()),
                customerId = payment.customerId,
                amount = payment.price,
                transactionType = transactionType,
            )
        )
    }

    private fun subtractCreditEntry(payment: Payment, creditEntry: CreditEntry) {
        creditEntry.subtractCreditAmount(payment.price)
    }

    private fun validateCreditEntry(
        payment: Payment,
        creditEntry: CreditEntry,
        failureMessages: MutableList<String>
    ) {
        if (payment.price.isGreaterThan(creditEntry.totalCreditAmount)) {
            logger.error("Customer with id: ${payment.customerId.value} doesn't have enough value for payment!")
            failureMessages.add("Customer with id=${payment.customerId.value} doesn't have enough credit for payment!")
        }
    }

    override fun validateAndCancelPayment(
        payment: Payment,
        creditEntry: CreditEntry,
        creditHistories: MutableList<CreditHistory>,
        failureMessages: MutableList<String>,
    ): PaymentEvent {
        payment.validatePayment(failureMessages)
        addCreditEntry(payment, creditEntry)
        updateCreditHistory(
            payment = payment,
            creditHistories = creditHistories,
            transactionType = TransactionType.CREDIT,
        )

        return when {
            failureMessages.isEmpty() -> {
                logger.info("Payment is cancelled for order id: ${payment.orderId.id}")
                payment.updateStatus(PaymentStatus.CANCELLED)
                return PaymentCancelledEvent(
                    payment = payment,
                    createdAt = ZonedDateTime.now(ZoneId.of(KOREA_DATE_TIME)),
                )
            }

            else -> {
                logger.info("Payment cancellation is failed for order id: ${payment.orderId.id}")
                payment.updateStatus(PaymentStatus.FAILED)
                return PaymentFailedEvent(
                    payment = payment,
                    createdAt = ZonedDateTime.now(ZoneId.of(KOREA_DATE_TIME)),
                    failureMessages = failureMessages,
                )
            }
        }
    }

    private fun addCreditEntry(payment: Payment, creditEntry: CreditEntry) {
        creditEntry.addCreditAmount(payment.price)
    }
}
