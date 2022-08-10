package test.geo.shortly.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import test.geo.shortly.R
import test.geo.shortly.getOrAwaitValueTest
import test.geo.shortly.ui.viewmodel.ShortlyViewModel

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ShortlyActivityTest {


    //adding test rules

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(ShortlyActivity::class.java)

    //dispatcher for coroutine in test environment
    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        hiltRule.inject()
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun testEvent() = runTest {
        var testViewModel: ShortlyViewModel? = null
        activityScenarioRule.scenario.onActivity {
            testViewModel = it.viewModel
        }
        val link = "https://asdfas.asdf"
        onView(withId(R.id.et_url_to_short)).perform(replaceText(link))
        onView(withId(R.id.tvShortenLink)).perform(click())
        val linkHistory = testViewModel?.linkHistory?.getOrAwaitValueTest()



        assertThat(linkHistory?.isEmpty()).isTrue()


    }


}