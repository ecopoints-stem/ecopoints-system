package br.edu.uea.ecopoints.data.api

import br.edu.uea.ecopoints.domain.entity.CoopAdmin
import br.edu.uea.ecopoints.domain.entity.Driver
import br.edu.uea.ecopoints.domain.entity.Employee
import br.edu.uea.ecopoints.domain.entity.model.UserApp
import br.edu.uea.ecopoints.domain.network.request.AdminRegister
import br.edu.uea.ecopoints.domain.network.request.DriverRegister
import br.edu.uea.ecopoints.domain.network.request.EmployeeRegister
import br.edu.uea.ecopoints.domain.network.request.ResetPasswordRequest
import br.edu.uea.ecopoints.domain.network.request.UserLogin
import br.edu.uea.ecopoints.domain.network.response.UserLoginTokens
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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
    suspend fun resetPasswordById(@Path("userId") userId: Long) : Response<String>
    @POST("/auth/{userId}/newPassword")
    suspend fun createNewPassword(@Path("userId") userId: Long, @Body resetPassword: ResetPasswordRequest) : Response<UserApp>

    // Rotas para /employee
    @GET("/employee/{id}")
    suspend fun findEmployeeById(@Path("id") id: Long) : Response<Employee>

    // Rotas para /driver
    @GET("/driver/{id}")
    suspend fun findDriverById(@Path("id") id: Long) : Response<Driver>

    // Rotas para /admin
    @GET("/admin/{id}")
    suspend fun findAdminById(@Path("id") id: Long) : Response<CoopAdmin>
}