package br.edu.uea.ecopoints

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import io.swagger.v3.oas.annotations.servers.Server

@SpringBootApplication
@OpenAPIDefinition(servers = [Server(url="/", description = "Default Server Url")])
class EcopointsApplication

fun main(args: Array<String>) {
	runApplication<EcopointsApplication>(*args)
}
