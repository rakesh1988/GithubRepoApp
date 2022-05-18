package com.example.github.repositories.dagger

import com.example.github.repositories.data.GITHUB_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {
    @Provides
    fun provideRetrofitClient(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(GITHUB_URL)
            .callFactory { okHttpClient.newCall(it) }
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

}