package com.food.ordering.system.outbox

interface OutboxScheduler {
    fun processOutboxMessage()
}
