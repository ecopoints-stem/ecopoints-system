package br.edu.uea.ecopoints.domain.network.response.page

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class PageResponse<T>(
    @JsonProperty("content") val content: List<T>,
    @JsonProperty("totalPages") val totalPages: Int,
    @JsonProperty("totalElements") val totalElements: Int,
    @JsonProperty("number") val number: Int
)
