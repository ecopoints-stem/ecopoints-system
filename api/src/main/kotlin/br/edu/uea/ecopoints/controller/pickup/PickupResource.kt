package br.edu.uea.ecopoints.controller.pickup

import br.edu.uea.ecopoints.domain.pickup.RecyclingPickupRequest
import br.edu.uea.ecopoints.domain.user.Driver
import br.edu.uea.ecopoints.dto.pickup.RecyclingPickupRequestRegister
import br.edu.uea.ecopoints.dto.pickup.RecyclingPickupRequestUpdate
import br.edu.uea.ecopoints.enums.PickupRequestStatus
import br.edu.uea.ecopoints.service.interf.pickup.IPickupRequestService
import br.edu.uea.ecopoints.service.interf.cooperative.ICooperativeService
import br.edu.uea.ecopoints.service.interf.user.ICoopAdmService
import br.edu.uea.ecopoints.service.interf.user.IDriverService
import br.edu.uea.ecopoints.view.pickup.RecyclingPickupRequestView
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

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

    @GetMapping("/driver/{driverId}")
    fun getRequestsByDriverId(
        @PathVariable driverId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "5") size: Int
    ) : Page<RecyclingPickupRequestView> {
        return pickUpService.findAllByDriverId(driverId, page, size).map { pickup -> pickup.toView() }
    }

    @GetMapping("/requester/{requesterId}")
    fun getRequestsByRequesterId(
        @PathVariable requesterId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "5") size: Int
    ) : Page<RecyclingPickupRequestView> {
        return pickUpService.findAllByRequesterId(requesterId, page, size).map { pickup -> pickup.toView() }
    }
    @PostMapping("/{id}/status")
    fun updatePickUpRequestStatus(@PathVariable id: Long, @RequestParam("newStatus") newStatus: PickupRequestStatus) : ResponseEntity<RecyclingPickupRequestView>{
        val pickup = pickUpService.findById(id)
        pickup.status = newStatus
        val pickupUpdated = pickUpService.save(pickup)
        return ResponseEntity.status(HttpStatus.OK).body(pickupUpdated.toView())
    }
    @PatchMapping("/list")
    fun updatePickUpRequests(@RequestBody listDto: List<RecyclingPickupRequestUpdate>) : ResponseEntity<List<RecyclingPickupRequestView>> {
        val listUpdated = mutableListOf<RecyclingPickupRequestView>()
        for (item in listDto){
            val pickup = pickUpService.findByIdWithDriverAndRequester(item.id)
            if(item.driverId!=pickup.driver?.id){
                var driver : Driver? = null
                item.driverId?.let { id ->
                    driver = driverService.findById(id)
                }
                pickup.driver = driver
            }
            pickup.status = item.status
            pickup.unitPrice = item.unitPrice
            pickup.pDate = item.requestDate
            val pickupUpdated = pickUpService.save(pickup)
            listUpdated.add(pickupUpdated.toView())
        }
        return ResponseEntity.status(HttpStatus.OK).body(listUpdated)
    }
    @GetMapping("/driver/{driverId}")
    fun getByDate(
        @PathVariable driverId: Long,
        @RequestParam("personDate") personDate: LocalDate,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "5") size: Int
    ) : Page<RecyclingPickupRequestView>{
        return pickUpService.findAllByDateAndDriverId(personDate, driverId, page, size).map { r -> r.toView() }
    }
}