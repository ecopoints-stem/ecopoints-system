package br.edu.uea.ecopoints.domain.network.request

import com.fasterxml.jackson.annotation.JsonProperty

data class MessageEmailSendNewPassword(
    @JsonProperty("message") val message: String
)
