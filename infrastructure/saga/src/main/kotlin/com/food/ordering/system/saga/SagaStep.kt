package com.food.ordering.system.saga


interface SagaStep<T> {
    fun process(data: T)
    fun rollback(data: T)
}
