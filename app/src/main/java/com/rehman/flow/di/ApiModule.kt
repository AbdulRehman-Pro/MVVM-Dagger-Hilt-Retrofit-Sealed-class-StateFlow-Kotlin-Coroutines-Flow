package com.rehman.flow.di

import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import com.rehman.flow.network.ApiService
import com.rehman.flow.utils.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


private const val REQUEST_TIMEOUT = 30000L

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Named("BASE_URL")
    fun provideBaseUrl(): String = AppConstants.BASE_URL

    @Provides
    @Singleton
    fun provideApiService(
        @Named("BASE_URL") baseurl: String,
        converterFactory: GsonConverterFactory,
        client: OkHttpClient,
    ): ApiService {

        return Retrofit.Builder()
            .baseUrl(baseurl)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(ApiService::class.java)

    }

    @Provides
    fun provideClient(
        @Named("AUTH_TOKEN") authToken: String?,
    ): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()

                authToken?.let { requestBuilder.addHeader("Authorization", it) }

                chain.proceed(requestBuilder.build())
            }
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideConvertorFactory(): GsonConverterFactory {
        val gson = GsonBuilder()
            .setStrictness(Strictness.LENIENT)
            .create()
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    @Named("AUTH_TOKEN")
    fun provideAuthToken(): String? {
        // Return the auth token from a secure source or a method that retrieves it
        return null
    }


}