package com.example.dotametrics.data.local.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.dotametrics.data.local.AppDatabase
import com.example.dotametrics.data.local.dao.PlayerDao
import com.example.dotametrics.domain.entity.local.PlayerDbModel
import com.example.dotametrics.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlayerRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var dao: PlayerDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.playerDao()
    }

    @Test
    fun playerInsertIsCorrect() = runTest {
        val player = PlayerDbModel(1500, "John", null)
        dao.insertPlayer(player)
        
        val players = dao.getPlayers().first()
        assert(players.isNotEmpty())
        assertEquals(player, players.first())
    }

    @Test
    fun playerInsertGetOrAwaitValueIsCorrect() = runTest {
        val player = PlayerDbModel(1500, "John", null)
        dao.insertPlayer(player)
        
        val expectedList = dao.getPlayers().asLiveData().getOrAwaitValue()
        assert(expectedList.isNotEmpty())
        assertEquals(player, expectedList.first())
    }

    @Test
    fun playerInsertAndDeleteIsCorrect() = runTest {
        val player = PlayerDbModel(1500, "John", null)
        dao.insertPlayer(player)
        dao.deletePlayer(1500)
        
        val players = dao.getPlayers().first()
        assert(players.isEmpty())
    }

    @Test
    fun playerInsertAndGetIsCorrect() = runTest {
        val player = PlayerDbModel(1500, "John", null)
        dao.insertPlayer(player)
        
        val actualPlayer = dao.getPlayer(1500)
        assertEquals(player, actualPlayer)
    }

    @After
    fun teardown() {
        db.close()
    }
}
