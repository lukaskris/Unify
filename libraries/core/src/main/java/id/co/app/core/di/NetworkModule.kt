package id.co.app.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.app.core.BuildConfig
import id.co.app.core.domain.interceptor.HttpRequestInterceptor
import id.co.app.core.domain.network.PokedexClient
import id.co.app.core.domain.network.PokedexService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

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
    fun providePokedexService(retrofit: Retrofit): PokedexService {
        return retrofit.create(PokedexService::class.java)
    }

    @Provides
    @Singleton
    fun providePokedexClient(pokedexService: PokedexService): PokedexClient {
        return PokedexClient(pokedexService)
    }
}