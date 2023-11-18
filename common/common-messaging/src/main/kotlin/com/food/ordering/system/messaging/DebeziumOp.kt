package com.food.ordering.system.messaging

enum class DebeziumOp(
    val value: String,
) {

    CREATE("c"), UPDATE("u"), DELETE("d");

}
