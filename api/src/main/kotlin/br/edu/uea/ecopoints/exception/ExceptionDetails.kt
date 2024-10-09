package br.edu.uea.ecopoints.exception

import br.edu.uea.ecopoints.enums.ExceptionDetailsStatus
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer
import java.time.LocalDateTime
import java.time.ZonedDateTime

data class ExceptionDetails (
    val title: String,
    val status: ExceptionDetailsStatus,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
    @JsonSerialize(using = LocalDateTimeSerializer::class)
    val timestamp: LocalDateTime,
    val exception: String,
    val details: MutableMap<String, String?>
)
