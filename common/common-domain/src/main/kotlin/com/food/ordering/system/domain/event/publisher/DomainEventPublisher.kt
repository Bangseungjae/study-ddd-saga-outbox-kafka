package com.food.ordering.system.domain.event.publisher

import com.food.ordering.system.domain.event.DomainEvent

interface DomainEventPublisher<T: DomainEvent<*>> {

    fun publish(domainEvent: T)
}
