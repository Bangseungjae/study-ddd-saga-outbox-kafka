package com.food.ordering.system.application.handler

import jakarta.validation.ConstraintViolationException
import jakarta.validation.ValidationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @ResponseBody
    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(exception: Exception): ErrorDto {
        logger.error(exception.message, exception)
        return ErrorDto(
            code = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
            message = "Unexpected error",
        )
    }


    @ResponseBody
    @ExceptionHandler(ValidationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(validationException: ValidationException): ErrorDto {

        when (validationException) {
            is ConstraintViolationException -> {
                val violations: String = extractViolationsFromException(validationException)
                logger.error(violations, validationException)
                return ErrorDto(
                    code = HttpStatus.BAD_REQUEST.reasonPhrase,
                    message = violations
                )
            }
            else -> {
                val exceptionMessage = validationException.message
                logger.error(exceptionMessage, validationException)
                return ErrorDto(
                    code = HttpStatus.BAD_REQUEST.reasonPhrase,
                    message = exceptionMessage,
                )
            }
        }
    }

    private fun extractViolationsFromException(validationException: ConstraintViolationException): String =
        validationException.constraintViolations.joinToString(separator = "--") { it.message }
}
