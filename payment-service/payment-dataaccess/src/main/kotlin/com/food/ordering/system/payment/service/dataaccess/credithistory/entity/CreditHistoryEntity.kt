package com.food.ordering.system.payment.service.dataaccess.credithistory.entity

import com.food.ordering.system.payment.service.domain.valueobject.TransactionType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.util.UUID

@Table(name = "credit_history")
@Entity
class CreditHistoryEntity(
    @Id
    val id: UUID,
    val customerId: UUID,
    val amount: BigDecimal,
    @Enumerated(EnumType.STRING)
    val type: TransactionType,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CreditHistoryEntity) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
