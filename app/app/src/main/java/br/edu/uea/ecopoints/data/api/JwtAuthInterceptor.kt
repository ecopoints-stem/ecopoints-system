package br.edu.uea.ecopoints.data.api

import android.content.SharedPreferences
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.nio.charset.Charset
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

        logRequestBody(original)

        val requestBuilder : Request.Builder = original
            .newBuilder()
            .header("Authorization",credentials)
        val request = requestBuilder.build()

        return chain.proceed(request)
    }

    private fun logRequestBody(request: Request) {
        try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body?.writeTo(buffer)

            val charset: Charset = Charset.forName("UTF-8")
            Log.d("ECO", "Request Body: ${buffer.readString(charset)}")
        } catch (e: Exception) {
            Log.e("ECO", "Error reading request body", e)
        }
    }
}