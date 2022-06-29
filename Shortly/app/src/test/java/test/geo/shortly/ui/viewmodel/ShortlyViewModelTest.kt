package test.geo.shortly.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
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
import test.geo.shortly.getOrAwaitValueTest
import test.geo.shortly.other.NetworkConnection
import test.geo.shortly.other.Status
import test.geo.shortly.repositories.ShortlyTestRepository

@ExperimentalCoroutinesApi
class ShortlyViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel: ShortlyViewModel

    @Before
    fun setUp() {
        viewModel = ShortlyViewModel(ShortlyTestRepository(), NetworkConnection(true))
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `add short link with empty link, return error` () = runTest {
        viewModel.addShortLink("")

        val result = viewModel.addShortLinkStatus.getOrAwaitValueTest()

        assertThat(result.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `add short link with invalid link, return error` () = runTest {
        viewModel.addShortLink("asdasdf")

        val result = viewModel.addShortLinkStatus.getOrAwaitValueTest()

        assertThat(result.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `add short link with valid link, return success` () = runTest {
        viewModel.addShortLink("asdasdf.")

        val result = viewModel.addShortLinkStatus.getOrAwaitValueTest()

        assertThat(result.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun `add short link when network error, return error` () = runTest {
        val repository = ShortlyTestRepository()
        repository.setShouldReturnNetworkError(true)
        viewModel = ShortlyViewModel(repository, NetworkConnection(true))
        viewModel.addShortLink("asdasdf.")

        val result = viewModel.addShortLinkStatus.getOrAwaitValueTest()

        assertThat(result.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `add short link with no network, return error` () = runTest {
        val repository = ShortlyTestRepository()
        viewModel = ShortlyViewModel(repository, NetworkConnection(false))
        viewModel.addShortLink("asdasdf.")

        val result = viewModel.addShortLinkStatus.getOrAwaitValueTest()

        assertThat(result.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

}