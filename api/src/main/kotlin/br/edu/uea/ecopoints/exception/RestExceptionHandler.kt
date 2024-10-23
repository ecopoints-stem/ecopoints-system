package br.edu.uea.ecopoints.exception

import br.edu.uea.ecopoints.enums.ExceptionDetailsStatus
import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class RestExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handlerValidException(ex: MethodArgumentNotValidException) : ResponseEntity<ExceptionDetails>{
        val errors: MutableMap<String,String?> = HashMap()
        ex.bindingResult.allErrors.stream().forEach { error : ObjectError ->
            val fieldName: String = (error as FieldError).field
            val messageError: String? = error.defaultMessage
            errors[fieldName] = messageError
        }
        return ResponseEntity(
            ExceptionDetails(
                title = "Bad Request! Consult the documentation",
                timestamp = LocalDateTime.now(),
                status = ExceptionDetailsStatus.INVALID_INPUT,
                exception = ex.javaClass.toString(),
                details = errors
            ), HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(DomainException::class)
    fun handlerDomainException(ex: DomainException) : ResponseEntity<ExceptionDetails>{
        val httpStatus: HttpStatus = when(ex.type){
            ExceptionDetailsStatus.AUTHENTICATION -> HttpStatus.UNAUTHORIZED
            ExceptionDetailsStatus.INVALID_INPUT -> HttpStatus.BAD_REQUEST
            ExceptionDetailsStatus.TOKEN_EXPIRED -> HttpStatus.UNAUTHORIZED
            ExceptionDetailsStatus.USER_NOT_FOUND -> HttpStatus.NOT_FOUND
            ExceptionDetailsStatus.EMAIL -> HttpStatus.BAD_REQUEST
        }
        return ResponseEntity.status(httpStatus).body(ExceptionDetails(
            title = "Error ${ex.message}",
            timestamp = LocalDateTime.now(),
            exception = ex.javaClass.toString(),
            status = ex.type,
            details = mutableMapOf(
                (ex.cause?.message ?: "erro de domínio") to ex.message
            )
        ))
    }

    @ExceptionHandler(DataAccessException::class)
    fun handlerValidException(ex: DataAccessException): ResponseEntity<ExceptionDetails> {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(
                ExceptionDetails(
                    title = "Conflict! Consult the documentation",
                    timestamp = LocalDateTime.now(),
                    status = ExceptionDetailsStatus.INVALID_INPUT,
                    exception = ex.javaClass.toString(),
                    details = mutableMapOf(ex.cause.toString() to ex.message)
                )
            )
    }
}