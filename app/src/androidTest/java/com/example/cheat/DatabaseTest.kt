package com.example.cheat
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cheat.model.AppDatabase
import com.example.cheat.model.Message
import com.example.cheat.model.MessageDao
import com.jraska.livedata.test
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.Instant
import java.util.*

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var messageDao: MessageDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java).build()
        messageDao = db.getMessageDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
        val message: Message = Message(0, "test", Date.from(Instant.now()), false)
        messageDao.insertAll(message)
        val queryResult : LiveData<List<Message>> = messageDao.loadAllMessages()
        println(message)
        queryResult.test()
            .awaitValue()
            .assertHasValue()
            .assertValue{it!!.get(0) == message }
    }
}