package br.edu.uea.ecopoints.data.api

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class JwtAuthInterceptor @Inject constructor(
    private val shared: SharedPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken: String = shared.getString("accessToken", "") ?: ""
        val credentials: String = "Bearer $accessToken"

        if(accessToken.isBlank() || accessToken.isEmpty()){
            return chain.proceed(chain.request())
        }

        val original: Request = chain.request()

        val requestBuilder : Request.Builder = original
            .newBuilder()
            .header("Authorization",credentials)
        val request = requestBuilder.build()

        return chain.proceed(request)
    }
}