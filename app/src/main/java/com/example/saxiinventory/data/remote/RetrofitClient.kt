package com.example.saxiinventory.data.remote

import com.example.saxiinventory.common.Constants.BASE_URL
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { provideOkHttpClient() }
    single { provideRetrofit(BASE_URL, get()) }
    single { provideApiService(get()) }
    single { RetrofitClient(get()) }
    single { RetrofitClient(get()).initialize() }
}

fun provideOkHttpClient(): OkHttpClient = OkHttpClient()
    .newBuilder()
    .connectTimeout(60, TimeUnit.SECONDS)
    .writeTimeout(60, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .build()

fun provideRetrofit(baseUrl: String, okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)


class RetrofitClient(private val retrofit: Retrofit) {
    lateinit var apiService: ApiService

    init {
        initialize()
    }

    fun initialize() {
        apiService = retrofit.create(ApiService::class.java)
    }
}