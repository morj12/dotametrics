package com.example.dotametrics.dota.remote.service

import com.example.dotametrics.domain.entity.remote.players.PlayersResult
import com.example.dotametrics.domain.entity.remote.players.Profile
import com.example.dotametrics.data.remote.service.DotaService
import com.example.dotametrics.data.remote.service.RetrofitInstance
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

class DotaServiceTest {

    private lateinit var service: DotaService

    @Before
    fun setup() {
        service = RetrofitInstance.getService()
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