package br.edu.uea.ecopoints.service.user

import br.edu.uea.ecopoints.domain.user.model.EcoUser

interface IUserService {
    fun findById(id: Long) : EcoUser
    fun existsById(id: Long) : Boolean
    fun findByEmail(email: String) : EcoUser
    fun resetPassword(user: EcoUser, newPassword: String) : EcoUser
}