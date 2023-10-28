package com.food.ordering.system.order.service.application.exception.handler

import com.food.ordering.system.application.handler.ErrorDto
import com.food.ordering.system.application.handler.GlobalExceptionHandler
import com.food.ordering.system.order.service.domain.exception.OrderDomainException
import com.food.ordering.system.order.service.domain.exception.OrderNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class OrderGlobalExceptionHandler : GlobalExceptionHandler() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OrderDomainException::class)
    fun handleException(orderDomainException: OrderDomainException): ErrorDto {
        logger.error(orderDomainException.message, orderDomainException)
        return ErrorDto(
            code = HttpStatus.BAD_REQUEST.reasonPhrase,
            message = orderDomainException.message,
        )
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(OrderNotFoundException::class)
    fun handleException(orderNotFoundException: OrderNotFoundException): ErrorDto {
        logger.error(orderNotFoundException.message, orderNotFoundException)
        return ErrorDto(
            code = HttpStatus.NOT_FOUND.reasonPhrase,
            message = orderNotFoundException.message,
        )
    }
}
