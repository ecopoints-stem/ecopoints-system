package br.edu.uea.ecopoints.data.api

import android.content.SharedPreferences
import android.util.Log
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.json.JSONObject
import javax.inject.Inject
import br.edu.uea.ecopoints.BuildConfig
import br.edu.uea.ecopoints.BuildConfig.BASE_URL_API
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class JwtAuthenticator @Inject constructor(
    private val shared: SharedPreferences
) : Authenticator {

    private val client = OkHttpClient()

    override fun authenticate(route: Route?, response: Response): Request? {
        //Método é executado automaticamente quando a requisição dá código 401 (Unauthorized)

        //Recuperar o refresh token armazenado no shared preferences e os dados do usuário
        val refreshToken: String = shared.getString("refreshToken","") ?: ""
        val userEmail: String = shared.getString("email", "") ?: ""
        val userPassword: String = shared.getString("password", "") ?: ""

        if(refreshToken.isNotBlank()
            && userEmail.isNotBlank()
            && userPassword.isNotBlank()){
            val refreshTokenRequest: Request =
                Request.Builder()
                    .url("$BASE_URL_API/auth/refresh").post(
                        createJsonObjectRefreshToken(refreshToken).toString()
                            .toRequestBody(
                                "application/json; charset=utf-8".toMediaType()
                            )
                    ).build()
            try {
                val refreshTokenResponse = client.newCall(refreshTokenRequest).execute()
                if(refreshTokenResponse.isSuccessful){
                    //API respondeu corretamente, coletar e armazenar o token
                    var newAccessToken : String = refreshTokenResponse.body?.string() ?: ""
                    val newAccessTokenObject : JSONObject = JSONObject(newAccessToken)
                    newAccessToken = newAccessTokenObject.getString("newAccessToken")
                    Log.i("ECO", "Novo accessToken $newAccessToken")
                    with(shared.edit()){
                        putString("accessToken",newAccessToken)
                        commit()
                    }
                    return response.request
                        .newBuilder()
                        .header("Authorization",
                            "Bearer $newAccessToken"
                        ).build()
                } else if(refreshTokenResponse.code==401){
                    //O refresh token expirou, pedir um novo em /auth
                    Log.i("ECO","Peguei 401 no /auth/refresh")
                    val loginTokenRequest: Request =
                        Request.Builder()
                            .url("$BASE_URL_API/auth").post(
                                createJsonObjectLogin(userEmail, userPassword).toString()
                                    .toRequestBody(
                                        "application/json; charset=utf-8".toMediaType()
                                    )
                            ).build()
                    val loginTokenResponse = client.newCall(loginTokenRequest).execute()
                    if(loginTokenResponse.isSuccessful){
                        val newLoginTokens : String = loginTokenResponse.body?.string() ?: ""
                        val newLoginObject : JSONObject = JSONObject(newLoginTokens)

                        //Recupera o access token
                        val accessToken: String = newLoginObject.getString("accessToken")
                        val newRefreshToken: String = newLoginObject.getString("refreshToken")
                        val id: Long = newLoginObject.getLong("id")
                        with(shared.edit()){
                            putString("accessToken", accessToken)
                            putString("refreshToken", newRefreshToken)
                            putLong("id",id)
                            commit()
                        }
                        Log.i("ECO","Todas as operações e salvou o novo token")
                        return response.request
                            .newBuilder()
                            .header("Authorization","Bearer $accessToken").build()
                    } else {
                        Log.i("ECO","Tomei erro no POST em /auth ${loginTokenResponse.code}")
                    }
                } else {
                    Log.i("ECO","JwtAuthenticator: Tomei erro no /auth pra fazer novo login ${refreshTokenResponse.code}")
                }
            } catch (e: IOException) {
                Log.e("ECO",e.message?:"Erro de IO")
                e.printStackTrace()
            }
        }
        return null
    }

    private fun createJsonObjectRefreshToken(refreshToken: String) : JSONObject {
        val jsonRefreshObject: JSONObject = JSONObject().apply {
            put("refreshToken", refreshToken)
        }
        return jsonRefreshObject
    }

    private fun createJsonObjectLogin(email: String, password: String) : JSONObject {
        val jsonLoginObject: JSONObject = JSONObject().apply {
            put("email", email)
            put("password", password)
        }
        return jsonLoginObject
    }
}