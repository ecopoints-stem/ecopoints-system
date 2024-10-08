package br.edu.uea.ecopoints.service.user

import br.edu.uea.ecopoints.domain.user.RecyclingSorter

interface IRecyclingSorterService {
    fun save(recyclingSorter: RecyclingSorter) : RecyclingSorter
    fun findById(id: Long) : RecyclingSorter
    fun existsById(id: Long) : Boolean
    fun deleteById(id: Long)
}