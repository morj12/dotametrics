package com.example.dotametrics.data.local.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.dotametrics.data.local.AppDatabase
import com.example.dotametrics.data.local.dao.PlayerDao
import com.example.dotametrics.domain.entity.local.PlayerDbModel
import com.example.dotametrics.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch

@ExperimentalCoroutinesApi
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

    /**
     * Using CountDownLatch
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun playerInsertIsCorrect() = runTest {
        val player = PlayerDbModel(1500, "John", null)
        dao.insertPlayer(player)
        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            dao.getPlayers().collect {
                assert(it.isNotEmpty())
                assertEquals(player, it.first())
                latch.countDown()
            }
        }
        latch.await()
        job.cancelAndJoin()
    }


    /**
     * Using getOrAwaitValue
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun playerInsertGetOrAwaitValueIsCorrect() = runBlockingTest {
        val player = PlayerDbModel(1500, "John", null)
        dao.insertPlayer(player)
        val expectedList = dao.getPlayers().asLiveData().getOrAwaitValue()
        assert(expectedList.isNotEmpty())
        assertEquals(player, expectedList.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun playerInsertAndDeleteIsCorrect() = runTest {
        val player = PlayerDbModel(1500, "John", null)
        dao.insertPlayer(player)
        dao.deletePlayer(1500)
        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            dao.getPlayers().collect {
                assert(it.isEmpty())
                latch.countDown()
            }
        }
        latch.await()
        job.cancelAndJoin()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
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