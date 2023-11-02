package com.food.ordering.system.payment.service.dataaccess.credithistory.mapper

import com.food.ordering.system.domain.valueobject.CustomerId
import com.food.ordering.system.domain.valueobject.Money
import com.food.ordering.system.payment.service.dataaccess.credithistory.entity.CreditHistoryEntity
import com.food.ordering.system.payment.service.domain.entity.CreditHistory
import com.food.ordering.system.payment.service.domain.valueobject.CreditHistoryId

fun CreditHistoryEntity.toCreditHistory(): CreditHistory = run {
    CreditHistory(
        creditHistoryId = CreditHistoryId(id),
        customerId = CustomerId(customerId),
        amount = Money(amount),
        transactionType = type,
    )
}

fun CreditHistory.toCreditHistoryEntity(): CreditHistoryEntity = run {
    CreditHistoryEntity(
        id = id.value,
        customerId = customerId.value,
        amount = amount.amount,
        type = transactionType,
    )
}
