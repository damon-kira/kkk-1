package com.kira.learning.network

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
import com.kira.learning.Constant
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kira.learning.base.keyvalue.SettingsManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/** 多 BaseUrl Qualifier 定义 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainBaseUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainRetrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // 仅保留主 BaseUrl
    @Provides
    @MainBaseUrl
    fun provideMainBaseUrl(): String = Constant.BASE_URL

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authProvider: InMemoryAuthTokenProvider,
        settingsManager: SettingsManager
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(NetworkConfig.CONNECT_TIMEOUT, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(NetworkConfig.READ_TIMEOUT, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(NetworkConfig.WRITE_TIMEOUT, java.util.concurrent.TimeUnit.SECONDS)
        .addInterceptor(HeaderInterceptor())
        .addInterceptor(AuthInterceptor(authProvider))
        .addInterceptor(Logout401Interceptor(settingsManager, authProvider))
        .addInterceptor(RetryInterceptor(NetworkConfig.MAX_RETRY_COUNT))
        .apply {
            if (NetworkConfig.ENABLE_LOGGING) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
        }
        .build()

    @Provides
    @Singleton
    @MainRetrofit
    fun provideMainRetrofit(
        @MainBaseUrl baseUrl: String,
        gson: Gson,
        client: OkHttpClient,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    @Singleton
    fun provideComposeApi(@MainRetrofit retrofit: Retrofit): ComposeApiService =
        retrofit.create(ComposeApiService::class.java)

    @Provides
    @Singleton
    fun provideImageLoader(
        @ApplicationContext context: Context,
        client: OkHttpClient,
    ): ImageLoader = ImageLoader.Builder(context)
        .okHttpClient(client)
        .crossfade(true)
        .build()

    @Provides
    @Singleton
    fun provideAuthTokenProvider(settingsManager: SettingsManager): InMemoryAuthTokenProvider {
        val p = InMemoryAuthTokenProvider(); p.updateToken(settingsManager.apiToken); return p
    }
}

class InMemoryAuthTokenProvider @Inject constructor() : AuthTokenProvider {
    @Volatile
    private var token: String? = null
    fun updateToken(t: String?) {
        token = t
    }

    override fun currentToken(): String? = token
}
