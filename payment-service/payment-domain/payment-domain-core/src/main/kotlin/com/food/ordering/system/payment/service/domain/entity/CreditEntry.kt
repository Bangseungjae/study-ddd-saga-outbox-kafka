package com.food.ordering.system.payment.service.domain.entity

import com.food.ordering.system.domain.entity.BaseEntity
import com.food.ordering.system.domain.valueobject.CustomerId
import com.food.ordering.system.domain.valueobject.Money
import com.food.ordering.system.payment.service.domain.valueobject.CreditEntryId

class CreditEntry(
    creditEntryId: CreditEntryId,
    val customerId: CustomerId,
    var totalCreditAmount: Money,
    val version: Int,
) : BaseEntity<CreditEntryId>(creditEntryId) {

    fun addCreditAmount(amount: Money) {
        totalCreditAmount = totalCreditAmount.add(amount)
    }

    fun subtractCreditAmount(amount: Money) {
        totalCreditAmount = totalCreditAmount.subtract(amount)
    }
}
