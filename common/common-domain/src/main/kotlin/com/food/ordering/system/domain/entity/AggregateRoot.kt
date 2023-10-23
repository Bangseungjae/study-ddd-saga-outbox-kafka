package com.food.ordering.system.domain.entity

abstract class AggregateRoot<ID>(
    value: ID,
) : BaseEntity<ID>(value)
