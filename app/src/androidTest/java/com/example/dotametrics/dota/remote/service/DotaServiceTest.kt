package com.example.dotametrics.dota.remote.service

import com.example.dotametrics.domain.entity.remote.players.PlayersResult
import com.example.dotametrics.domain.entity.remote.players.Profile
import com.example.dotametrics.data.remote.service.DotaService
import com.example.dotametrics.di.AppModule
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
    private val RETROFIT_URL = "https://api.opendota.com/api/"

    @Before
    fun setup() {
        service = Retrofit.Builder()
            .baseUrl(RETROFIT_URL)
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
        val expectedResult = PlayersResult(
            80,
            null,
            Profile(
                accountId = 841150476,
                personaname = "=)",
                steamid = "76561198801416204"
            )
        )

        val actualResult = service.getPlayersResults("841150476").execute().body()

        assertEquals(
            expectedResult.profile!!.steamid,
            actualResult?.profile!!.steamid
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getPlayerResultsReturnsIncorrectInformation() = runTest {
        val expectedResult = PlayersResult(
            80,
            null,
            Profile(
                accountId = 841150476,
                personaname = "=)",
                steamid = "abcd"
            )
        )

        val actualResult = service.getPlayersResults("841150476").execute().body()

        assertNotEquals(
            expectedResult.profile!!.steamid,
            actualResult?.profile!!.steamid
        )
    }
}