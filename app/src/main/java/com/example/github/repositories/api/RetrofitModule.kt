package com.example.github.repositories.api

import com.example.github.repositories.data.GITHUB_URL
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {
    @Provides
    fun provideRetrofitClient(): Retrofit =
        Retrofit.Builder()
            .baseUrl(GITHUB_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

}