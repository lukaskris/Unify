package id.co.app.source.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.app.source.core.BuildConfig
import id.co.app.source.core.base.base.BaseDispatchers
import id.co.app.source.core.base.base.Dispatchers
import id.co.app.source.core.domain.interceptor.HttpRequestInterceptor
import id.co.app.source.core.domain.network.AppClient
import id.co.app.source.core.domain.network.AppClientImpl
import id.co.app.source.core.domain.network.AppService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideDispatchers(): Dispatchers = BaseDispatchers()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpRequestInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideService(retrofit: Retrofit): AppService {
        return retrofit.create(AppService::class.java)
    }

    @Provides
    @Singleton
    fun provideClient(service: AppService): AppClient {
        return AppClientImpl(service)
    }
}