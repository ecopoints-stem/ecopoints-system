package br.edu.uea.ecopoints.data.api

import br.edu.uea.ecopoints.domain.entity.CoopAdmin
import br.edu.uea.ecopoints.domain.entity.Cooperative
import br.edu.uea.ecopoints.domain.entity.Driver
import br.edu.uea.ecopoints.domain.entity.Employee
import br.edu.uea.ecopoints.domain.entity.Material
import br.edu.uea.ecopoints.domain.entity.PickUpRequest
import br.edu.uea.ecopoints.domain.entity.SeparatedMaterial
import br.edu.uea.ecopoints.domain.entity.enums.PickupRequestStatus
import br.edu.uea.ecopoints.domain.entity.model.UserApp
import br.edu.uea.ecopoints.domain.network.request.AdminRegister
import br.edu.uea.ecopoints.domain.network.request.AdminUpdate
import br.edu.uea.ecopoints.domain.network.request.AttendanceRecordRegister
import br.edu.uea.ecopoints.domain.network.request.DriverRegister
import br.edu.uea.ecopoints.domain.network.request.DriverUpdate
import br.edu.uea.ecopoints.domain.network.request.EmployeeRegister
import br.edu.uea.ecopoints.domain.network.request.EmployeeUpdate
import br.edu.uea.ecopoints.domain.network.request.MessageEmailSendNewPassword
import br.edu.uea.ecopoints.domain.network.request.PickUpRegister
import br.edu.uea.ecopoints.domain.network.request.ResetPasswordRequest
import br.edu.uea.ecopoints.domain.network.request.SeparatedMaterialRegister
import br.edu.uea.ecopoints.domain.network.request.UserLogin
import br.edu.uea.ecopoints.domain.network.response.AttendanceRecord
import br.edu.uea.ecopoints.domain.network.response.BarDataAPI
import br.edu.uea.ecopoints.domain.network.response.UserId
import br.edu.uea.ecopoints.domain.network.response.UserLoginTokens
import br.edu.uea.ecopoints.domain.network.response.page.PageResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface EcoApi {
    // Rotas para /auth
    @POST("/auth")
    suspend fun login(@Body userInfo: UserLogin) : Response<UserLoginTokens>
    @POST("/auth/admin")
    suspend fun createAdmin(@Body admin: AdminRegister) : Response<CoopAdmin>
    @POST("/auth/employee")
    suspend fun createEmployee(@Body employee: EmployeeRegister) : Response<Employee>
    @POST("/auth/driver")
    suspend fun createDriver(@Body driver: DriverRegister) : Response<Driver>
    @POST("/auth/resetPassword/{userId}")
    suspend fun resetPasswordById(@Path("userId") userId: Long) : Response<MessageEmailSendNewPassword>
    @POST("/auth/{userId}/newPassword")
    suspend fun createNewPassword(@Path("userId") userId: Long, @Body resetPassword: ResetPasswordRequest) : Response<UserApp>
    @GET("/auth/userId/{email}")
    suspend fun getUserIdByEmail(@Path("email") email: String) : Response<UserId>

    // Rotas para /employee
    @GET("/employee/{id}")
    suspend fun findEmployeeById(@Path("id") id: Long) : Response<Employee>
    @POST("/employee/{id}/attendance")
    suspend fun clockInClockOut(@Path("id") id: Long, @Body hr: AttendanceRecordRegister) : Response<AttendanceRecord>
    @POST("/employee/{id}/separated")
    suspend fun createNewSeparatedMaterial(@Path("id") id: Long, @Body spMaterial: SeparatedMaterialRegister) : Response<SeparatedMaterial>
    @GET("/employee/materials")
    suspend fun getAllSpMaterialByEmployeeId(
        @Query("employeeId") employeeId: Long,
        @Query("page") page: Int,
        @Query("size") size: Int
    ) : Response<PageResponse<SeparatedMaterial>>
    @PATCH("/employee")
    suspend fun updateEmployee(@Query("employeeId") employeeId: Long, @Body employeeUpdate: EmployeeUpdate) : Response<Employee>

    // Rotas para /driver
    @GET("/driver/{id}")
    suspend fun findDriverById(@Path("id") id: Long) : Response<Driver>
    @PATCH("/driver")
    suspend fun updateDriver(@Query("driverId") id: Long, @Body driverUpdate: DriverUpdate) : Response<Driver>

    // Rotas para /admin
    @GET("/admin/{id}")
    suspend fun findAdminById(@Path("id") id: Long) : Response<CoopAdmin>
    @POST("/admin/{id}/material")
    suspend fun addNewMaterialForCooperative(@Path("id") id: Long,@Body material: Material) : Response<List<Material>>
    @PATCH("/admin")
    suspend fun updateAdmin(@Query("adminId") adminId: Long, @Body adminUpdate: AdminUpdate) : Response<CoopAdmin>
    @GET("/admin/{id}/bar")
    suspend fun getBarData(@Path("id") id: Long, @Query("endDate") endDate: String) : Response<BarDataAPI>
    @GET("/admin/{id}/report")
    suspend fun adminReport(@Path("id") id: Long, @Query("endDate") endDate: String) : Response<Unit>

    // Rotas para /pickup
    @POST("/pickup")
    suspend fun createPickUpRequest(@Body pickup: PickUpRegister) : Response<PickUpRequest>
    @GET("/pickup/client/{clientId}")
    suspend fun getAllRequestsByCooperativeAdminId(
        @Path("clientId") clientId: Long,
        @Query("page") page: Int,
        @Query("size") size: Int
    ) : Response<PageResponse<PickUpRequest>>
    @GET("/pickup/driver/{driverId}")
    suspend fun getAllRequestsByDriverId(
        @Path("driverId") driverId: Long,
        @Query("page") page: Int,
        @Query("size") size: Int
    ) : Response<PageResponse<PickUpRequest>>
    @GET("/pickup/driver/{driverId}/date")
    suspend fun getAllRequestsByDateAndDriverId(
        @Path("driverId") driverId: Long,
        @Query("personDate") personDate: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ) : Response<PageResponse<PickUpRequest>>
    @GET("/pickup/driver/{driverId}/date/status")
    suspend fun getAllRequestsByDriverIdAnDateAndPickUpStatus(
        @Path("driverId") driverId: Long,
        @Query("pickDate") pickDate: String,
        @Query("statuses") statuses: List<String>,
        @Query("page") page: Int,
        @Query("size") size: Int
    ) : Response<PageResponse<PickUpRequest>>
    @POST("/pickup/{id}/status")
    suspend fun updatePickUpStatus(
        @Path("id") pickUpId: Long,
        @Query("newStatus") newStatus: PickupRequestStatus
    ) : Response<PickUpRequest>
    //Chamar a função abaixo pra listar os pickup request do driver na tela de agendados
    @GET("/pickup/driver/{driverId}/all")
    suspend fun getAllRequestsByDriverIdAndPickUpStatusIn(
        @Path("driverId") driverId: Long,
        @Query("statuses") statuses: List<String>,
        @Query("page") page: Int,
        @Query("size") size: Int
    ) : Response<PageResponse<PickUpRequest>>

    // Rotas para /material
    @GET("/material/list")
    suspend fun getMaterialStartingWithName(
        @Query("name") name: String
    ) : Response<List<Material>>

    // Rotas para /cooperative
    @GET("/cooperative/{id}")
    suspend fun getCooperativeById(@Path("id") cooperativeId: Long) : Response<Cooperative>
}