package br.edu.uea.ecopoints.di.network

import android.annotation.SuppressLint
import android.content.SharedPreferences
import br.edu.uea.ecopoints.BuildConfig.BASE_URL_API
import br.edu.uea.ecopoints.data.api.EcoApi
import br.edu.uea.ecopoints.data.api.JwtAuthInterceptor
import br.edu.uea.ecopoints.data.api.JwtAuthenticator
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.security.cert.X509Certificate
import javax.inject.Singleton
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
object DataNetworkModule {
    @Provides
    @Singleton
    fun provideAuthenticator(shared: SharedPreferences) : JwtAuthenticator {
        return JwtAuthenticator(shared)
    }

    @Provides
    @Singleton
    fun provideInterceptor(shared: SharedPreferences) : JwtAuthInterceptor {
        return JwtAuthInterceptor(shared)
    }

    @Provides
    @Singleton
    fun provideHttClient(authenticator: JwtAuthenticator, interceptor: JwtAuthInterceptor) : OkHttpClient{
        val trustAllCerts = arrayOf<TrustManager>(
            @SuppressLint("CustomX509TrustManager")
            object : X509TrustManager {
                @SuppressLint("TrustAllX509TrustManager")
                override fun checkClientTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {}
                @SuppressLint("TrustAllX509TrustManager")
                override fun checkServerTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            }
        )
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())

        val sslSocketFactory = sslContext.socketFactory
        return OkHttpClient
            .Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier(HostnameVerifier { _, _ -> true })
            .addInterceptor(interceptor)
            .authenticator(authenticator)
            .build()
    }

    @Provides
    @Singleton
    fun providesObjectMapper() : ObjectMapper{
        return ObjectMapper().apply {
            registerModule(JavaTimeModule())
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, mapper: ObjectMapper) : Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL_API).addConverterFactory(
                JacksonConverterFactory.create(
                    mapper
                )
            ).client(client).build()
    }

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit) : EcoApi {
        return retrofit.create(EcoApi::class.java)
    }
}