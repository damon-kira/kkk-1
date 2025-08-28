package com.kira.learning.compose.network

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
import javax.inject.Qualifier
import javax.inject.Singleton
import com.kira.learning.compose.module.sample.SampleApi

/** 多 BaseUrl Qualifier 定义 */
@Qualifier @Retention(AnnotationRetention.BINARY) annotation class SampleBaseUrl
@Qualifier @Retention(AnnotationRetention.BINARY) annotation class SampleRetrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // BaseUrl 注入，后续可继续扩展新的 @Qualifier
    @Provides @SampleBaseUrl fun provideSampleBaseUrl(): String = "https://jsonplaceholder.typicode.com/"

    @Provides @Singleton fun provideGson(): Gson = GsonBuilder().create()

    @Provides @Singleton fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
        .build()

    @Provides @Singleton @SampleRetrofit
    fun provideSampleRetrofit(
        @SampleBaseUrl baseUrl: String,
        gson: Gson,
        client: OkHttpClient,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides @Singleton
    fun provideSampleApi(@SampleRetrofit retrofit: Retrofit): SampleApi = retrofit.create(SampleApi::class.java)
}

