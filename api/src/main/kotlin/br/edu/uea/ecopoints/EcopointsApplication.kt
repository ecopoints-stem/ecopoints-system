package br.edu.uea.ecopoints

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EcopointsApplication

fun main(args: Array<String>) {
	runApplication<EcopointsApplication>(*args)
}
