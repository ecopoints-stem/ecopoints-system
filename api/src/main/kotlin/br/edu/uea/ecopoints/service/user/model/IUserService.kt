package br.edu.uea.ecopoints.service.user.model

import br.edu.uea.ecopoints.domain.user.model.EcoUser

interface IUserService {
    fun save(user: EcoUser) : EcoUser
    fun findById(id: Long) : EcoUser
    fun existsById(id: Long) : Boolean
    fun findByEmail(email: String) : EcoUser
    fun resetPassword(user: EcoUser, newPassword: String) : EcoUser
}