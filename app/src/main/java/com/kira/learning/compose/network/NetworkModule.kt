package com.kira.learning.compose.network

import android.content.Context
import coil.ImageLoader
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
import com.kira.learning.Constant
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import com.kira.ui.core.storage.keyvalue.SettingsManager

/** 多 BaseUrl Qualifier 定义 */
@Qualifier @Retention(AnnotationRetention.BINARY) annotation class SampleBaseUrl
@Qualifier @Retention(AnnotationRetention.BINARY) annotation class SampleRetrofit
@Qualifier @Retention(AnnotationRetention.BINARY) annotation class MainBaseUrl
@Qualifier @Retention(AnnotationRetention.BINARY) annotation class MainRetrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // BaseUrl 注入，后续可继续扩展新的 @Qualifier
    @Provides @SampleBaseUrl fun provideSampleBaseUrl(): String = "https://jsonplaceholder.typicode.com/"
    @Provides @MainBaseUrl fun provideMainBaseUrl(): String = Constant.BASE_URL

    @Provides @Singleton fun provideGson(): Gson = GsonBuilder().create()

    @Provides @Singleton fun provideOkHttpClient(authProvider: InMemoryAuthTokenProvider): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HeaderInterceptor())
        .addInterceptor(AuthInterceptor(authProvider))
        .addInterceptor(RetryInterceptor())
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

    @Provides @Singleton @MainRetrofit
    fun provideMainRetrofit(
        @MainBaseUrl baseUrl: String,
        gson: Gson,
        client: OkHttpClient,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides @Singleton
    fun provideSampleApi(@SampleRetrofit retrofit: Retrofit): SampleApi = retrofit.create(SampleApi::class.java)

    @Provides @Singleton
    fun provideComposeApi(@MainRetrofit retrofit: Retrofit): ComposeApiService = retrofit.create(ComposeApiService::class.java)

    @Provides @Singleton fun provideImageLoader(
        @ApplicationContext context: Context,
        client: OkHttpClient,
    ): ImageLoader = ImageLoader.Builder(context)
        .okHttpClient(client)
        .crossfade(true)
        .build()

    @Provides @Singleton fun provideAuthTokenProvider(settingsManager: SettingsManager): InMemoryAuthTokenProvider {
        val p = InMemoryAuthTokenProvider(); p.updateToken(settingsManager.apiToken); return p
    }
}

class InMemoryAuthTokenProvider @javax.inject.Inject constructor(): AuthTokenProvider {
    @Volatile private var token: String? = null
    fun updateToken(t: String?) { token = t }
    override fun currentToken(): String? = token
}
