package com.kira.learning.compose.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val SAMPLE_BASE_URL = "https://jsonplaceholder.typicode.com/"

@Module
@InstallIn(SingletonComponent::class)
object SampleNetworkModule {

    @Provides @Singleton fun provideSampleGson(): Gson = GsonBuilder().create()

    @Provides @Singleton fun provideSampleOkHttp(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
        .build()

    @Provides @Singleton fun provideSampleRetrofit(gson: Gson, client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(SAMPLE_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides @Singleton fun provideSampleApi(retrofit: Retrofit): SampleApi =
        retrofit.create(SampleApi::class.java)
}

