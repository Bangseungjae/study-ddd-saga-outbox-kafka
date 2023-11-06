package com.food.ordering.system.domain.event

class EmptyEvent : DomainEvent<Unit> {

    companion object {
        val INSTANCE = EmptyEvent()
    }

    fun fire() {

    }
}
