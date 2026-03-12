package com.example.dotametrics.dota.remote.service

import com.example.dotametrics.data.remote.service.DotaService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class DotaServiceTest {

    private lateinit var service: DotaService
    private val retrofitUrl = "https://api.opendota.com/api/"

    @Before
    fun setup() {
        service = Retrofit.Builder()
            .baseUrl(retrofitUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient().newBuilder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build()
            )
            .build()
            .create(DotaService::class.java)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getPlayerResultsReturnsCorrectInformation() = runTest {
        val expectedSteamId = "76561198801416204"

        val response = service.getPlayersResults("841150476")
        val actualResult = response.body()

        assertEquals(
            expectedSteamId,
            actualResult?.profile?.steamId
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getPlayerResultsReturnsIncorrectInformation() = runTest {
        val wrongSteamId = "abcd"

        val response = service.getPlayersResults("841150476")
        val actualResult = response.body()

        assertNotEquals(
            wrongSteamId,
            actualResult?.profile?.steamId
        )
    }
}
