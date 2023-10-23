package com.food.ordering.system.domain.exception

open class DomainException : RuntimeException {
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)


}
