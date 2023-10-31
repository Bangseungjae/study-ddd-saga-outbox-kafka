package com.food.ordering.system.payment.service.domain.entity

import com.food.ordering.system.domain.entity.BaseEntity
import com.food.ordering.system.domain.valueobject.CustomerId
import com.food.ordering.system.domain.valueobject.Money
import com.food.ordering.system.payment.service.domain.valueobject.CreditHistoryId
import com.food.ordering.system.payment.service.domain.valueobject.TransactionType

class CreditHistory(
    creditHistoryId: CreditHistoryId,
    val customerId: CustomerId,
    val amount: Money,
    val transactionType: TransactionType,
) : BaseEntity<CreditHistoryId>(creditHistoryId) {
}
