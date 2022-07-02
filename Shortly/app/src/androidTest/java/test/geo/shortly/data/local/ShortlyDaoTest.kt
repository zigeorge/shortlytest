package test.geo.shortly.data.local

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class ShortlyDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_shortly_db")
    lateinit var database: ShortlyDatabase
    private lateinit var dao: ShortlyDao

    @Before
    fun setUp() {
        hiltRule.inject()
        dao = database.shortlyDao()
    }

    @After
    fun tearDown() {
        database.close()
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
        assertThat(link).isIn(allLink)
    }

    @Test
    fun deleteShareLink() = runTest {

        val link = ShortLink(
            "http://jjj.kkk.lll",
            "sco.ll/ky",
            id = 1
        )

        dao.insertLink(link)

        dao.deleteLink(link)

        val allLink = dao.observeAllLink().first()

        assertThat(link).isNotIn(allLink)
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