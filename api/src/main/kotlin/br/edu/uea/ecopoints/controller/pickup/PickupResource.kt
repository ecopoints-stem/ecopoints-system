package br.edu.uea.ecopoints.controller.pickup

import br.edu.uea.ecopoints.domain.pickup.RecyclingPickupRequest
import br.edu.uea.ecopoints.dto.pickup.RecyclingPickupRequestRegister
import br.edu.uea.ecopoints.enums.PickupRequestStatus
import br.edu.uea.ecopoints.service.interf.pickup.IPickupRequestService
import br.edu.uea.ecopoints.service.interf.cooperative.ICooperativeService
import br.edu.uea.ecopoints.service.interf.user.ICoopAdmService
import br.edu.uea.ecopoints.service.interf.user.IDriverService
import br.edu.uea.ecopoints.view.pickup.RecyclingPickupRequestView
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/pickup")
@Tag(name = "Pickup Resource")
class PickupResource (
    private val pickUpService : IPickupRequestService,
    private val coopService : ICooperativeService,
    private val admService: ICoopAdmService,
    private val driverService: IDriverService
){
    @PostMapping
    fun create(@RequestBody @Valid dto: RecyclingPickupRequestRegister) : ResponseEntity<RecyclingPickupRequestView>{
        val driver = driverService.findByEmail(dto.emailDriver)
        val adminRequesterId = coopService.findByCnpjWithAdministrator(dto.cnpj).adm?.id ?: -1
        val adminRequester = admService.findById(adminRequesterId)

        val pickUp = RecyclingPickupRequest(
            id = null, materialType = dto.materialType,
            quantity = dto.quantity, unitPrice = dto.unitPrice,
            address = dto.address, pDate = dto.requestDate,
            status = PickupRequestStatus.IN_PROGRESS,
            driver = driver, requester = adminRequester
        )

        val pickUpId = pickUpService.save(pickUp).id ?: -1
        val pickUpSaved = pickUpService.findByIdWithDriverAndRequester(pickUpId)
        return ResponseEntity.status(HttpStatus.CREATED).body(pickUpSaved.toView())
    }
}