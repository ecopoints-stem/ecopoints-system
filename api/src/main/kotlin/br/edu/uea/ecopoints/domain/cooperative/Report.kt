package br.edu.uea.ecopoints.domain.cooperative

import br.edu.uea.ecopoints.domain.user.CooperativeAdministrator

data class Report (
    val cooperative: Cooperative? = null,
    val administrator: CooperativeAdministrator? = null,
)