package com.splearn.adapter

import com.splearn.domain.member.DuplicateEmailException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class ApiControllerAdvice: ResponseEntityExceptionHandler() {

    @ExceptionHandler(Exception::class)
    fun exceptionHandler(exception: Exception): ProblemDetail {
        return generateProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception)
    }

    @ExceptionHandler(DuplicateEmailException::class)
    fun emailExceptionHandler(duplicateEmailException: DuplicateEmailException): ProblemDetail {
        return generateProblemDetail(HttpStatus.CONFLICT, duplicateEmailException)
    }

    private fun generateProblemDetail(status: HttpStatus, exception: Exception): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(status, exception.message)

        problemDetail.setProperty("timestamp", LocalDateTime.now())
        problemDetail.setProperty("exception", exception.javaClass.simpleName)

        return problemDetail
    }
}
