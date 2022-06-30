package test.geo.shortly.data.local

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@SmallTest
@RunWith(AndroidJUnit4::class)
class ShortlyDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ShortlyDatabase
    private lateinit var dao: ShortlyDao

    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            ShortlyDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.shortlyDao()
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        database.close()
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun insertShortLink() = runTest {

        val link = ShortLink(
            "http://jjj.kkk.lll",
            "sco.ll/ky",
            id = 1
        )
        dao.insertLink(link)

        val allLink = dao.observeAllLink().first()
        var contains = false
        allLink.forEach {
            if (it.id == link.id) {
                contains = true
            }
        }
        assertThat(contains).isTrue()
    }

    @Test
    fun deleteShareLink() = runTest {

        val link = ShortLink(
            "http://jjj.kkk.lll",
            "sco.ll/ky",
            id = 1
        )

        dao.deleteLink(link)

        val allLink = dao.observeAllLink().first()
        var contains = false
        if(allLink.isNotEmpty()) {
            allLink.forEach {
                if(it == link) {
                    contains = true
                }
            }
        }
        assertThat(contains).isFalse()
    }

    @Test
    fun observeAllLinkWhenListIsEmpty_ReturnsEmpty() = runTest {
        val allLink = dao.observeAllLink().first()

        assertThat(allLink).isEmpty()
    }

    @Test
    fun observeAllLinkWhenListIsNotEmpty_ReturnsNotEmpty() = runTest {
        val allLink = dao.observeAllLink()

        val link = ShortLink(
            "http://jjj.kkk.lll",
            "sco.ll/ky",
            id = 1
        )
        dao.insertLink(link)

        assertThat(allLink.first()).isNotEmpty()
    }



}