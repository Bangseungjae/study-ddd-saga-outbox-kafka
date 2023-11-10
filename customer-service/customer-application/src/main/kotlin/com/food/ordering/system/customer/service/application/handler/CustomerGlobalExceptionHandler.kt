package com.food.ordering.system.customer.service.application.handler

import com.food.ordering.system.application.handler.ErrorDto
import com.food.ordering.system.application.handler.GlobalExceptionHandler
import com.food.ordering.system.customer.service.domain.exception.CustomerDomainException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class CustomerGlobalExceptionHandler : GlobalExceptionHandler() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @ResponseBody
    @ExceptionHandler(value = [CustomerDomainException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(exception: CustomerDomainException): ErrorDto {
        logger.error(exception.message, exception)
        return ErrorDto(
            code = HttpStatus.BAD_REQUEST.reasonPhrase,
            message = exception.message
        )
    }
}
