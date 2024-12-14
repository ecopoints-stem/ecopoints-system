package br.edu.uea.ecopoints.domain.network.response.page

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class PageResponse<T>(
    @JsonProperty("content") val content: List<T>,
    @JsonProperty("pageable") val pageable: Pageable,
    @JsonProperty("totalPages") val totalPages: Int,
    @JsonProperty("totalElements") val totalElements: Int,
    @JsonProperty("last") val last: Boolean,
    @JsonProperty("first") val first: Boolean
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Pageable(
    @JsonProperty("pageNumber") val pageNumber: Int,
    @JsonProperty("pageSize") val pageSize: Int
)