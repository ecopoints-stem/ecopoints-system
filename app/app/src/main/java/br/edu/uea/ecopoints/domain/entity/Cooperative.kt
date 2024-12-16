package br.edu.uea.ecopoints.domain.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class Cooperative (
    @JsonProperty("id") val id: Long,
    @JsonProperty("name") val name: String,
    @JsonProperty("cnpj") val cnpj: String,
    @JsonProperty("adminId") val adminId: Long?,
    @JsonProperty("employeesId") val employeesId : List<Long>,
    @JsonProperty("materialsId") val materialsId : List<Long>
)
