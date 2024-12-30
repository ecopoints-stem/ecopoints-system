package br.edu.uea.ecopoints.domain.network.request

import br.edu.uea.ecopoints.domain.entity.enums.MaterialType
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalDate

data class PickUpRegister (
    @JsonProperty("requesterCnpj")
    val requesterCnpj: String,
    @JsonProperty("cooperativeAdminId")
    val cooperativeAdminId: Long,
    @JsonProperty("emailDriver")
    val emailDriver: String,
    @JsonProperty("address")
    val address: String,
    @JsonProperty("materialType")
    val materialType: MaterialType,
    @JsonProperty("quantity")
    val quantity: Double,
    @JsonProperty("unitPrice")
    val unitPrice: BigDecimal,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonProperty("requestDate")
    val requestDate : LocalDate
)