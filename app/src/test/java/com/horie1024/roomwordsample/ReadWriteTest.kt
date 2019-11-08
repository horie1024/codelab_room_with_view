package com.horie1024.roomwordsample

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ReadWriteTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var db: WordRoomDatabase
    private lateinit var wordDao: WordDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, WordRoomDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        wordDao = db.wordDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeReadTest() {

        val sampleWord = Word("test")
        val sampleWord2 = Word("test2")
        val sampleWord3 = Word("test3")
        runBlockingTest {
            wordDao.insert(sampleWord)
            wordDao.insert(sampleWord2)
            wordDao.insert(sampleWord3)
        }

        wordDao.getAllWords().observeForever {
            val word = it.getOrNull(0)
            assertThat(word?.word).isEqualTo("test")

            val word2 = it.getOrNull(1)
            assertThat(word2?.word).isEqualTo("test2")

            val word3 = it.getOrNull(2)
            assertThat(word3?.word).isEqualTo("test3")
        }
    }
}