package com.food.ordering.system.domain.valueobject

import java.math.BigDecimal
import java.math.RoundingMode

data class Money(
    val amount: BigDecimal
) {
    fun isGreaterThanZero(): Boolean = this.amount > BigDecimal.ZERO

    fun isGreaterThan(money: Money): Boolean = this.amount > money.amount

    fun add(money: Money): Money = Money(setScale(this.amount.add(money.amount)))

    private fun setScale(input: BigDecimal): BigDecimal = input.setScale(2, RoundingMode.HALF_EVEN)

    fun subtract(money: Money): Money = Money(setScale(this.amount.subtract(money.amount)))

    fun multiply(multiplier: Int): Money = Money(setScale(this.amount.multiply(BigDecimal(multiplier))))

    companion object {
        val ZERO: Money = Money(BigDecimal.ZERO)
    }

}

