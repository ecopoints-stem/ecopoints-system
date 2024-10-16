package br.edu.uea.ecopoints.data.api.exception

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class ExceptionDetails (
    @JsonProperty("title") val title: String,
    @JsonProperty("status") val status: ExceptionDetailsStatus,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
    @JsonProperty("timestamp")
    val timestamp: LocalDateTime,
    @JsonProperty("exception")
    val exception: String,
    @JsonProperty("details")
    val details: MutableMap<String, String?>
)