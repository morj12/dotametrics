package com.example.dotametrics.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.dotametrics.App
import com.example.dotametrics.data.local.AppDatabase
import com.example.dotametrics.data.local.repository.PlayerRepository
import com.example.dotametrics.data.remote.repository.OpenDotaRepository
import com.example.dotametrics.data.remote.service.DotaService
import com.example.dotametrics.domain.repository.IOpenDotaRepository
import com.example.dotametrics.domain.repository.IPlayerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val RETROFIT_URL = "https://api.opendota.com/api/"
    private const val DB_NAME = "dota_metrics_db"

    // TODO: different error listener for each request. reload on error
    // TODO: use repository for ConstData

    @Provides
    @Singleton
    fun providesDotaService(httpClient: OkHttpClient): DotaService = Retrofit.Builder()
        .baseUrl(RETROFIT_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()
        .create(DotaService::class.java)

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient =
        OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun providesOpenDotaRepository(dotaService: DotaService): IOpenDotaRepository =
        OpenDotaRepository(dotaService)

    @Provides
    @Singleton
    fun providesAppDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        DB_NAME
    ).build()

    @Provides
    @Singleton
    fun providesApp(application: Application): App =
        application as App

    @Provides
    @Singleton
    fun providesPlayerRepository(database: AppDatabase): IPlayerRepository =
        PlayerRepository(database)
}