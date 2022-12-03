package id.ergun.mystoryapp.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import id.ergun.mystoryapp.domain.usecase.story.paging.StoryListUseCase
import id.ergun.mystoryapp.presentation.model.StoryDummy
import id.ergun.mystoryapp.presentation.ui.story.list.StoryListAdapter
import id.ergun.mystoryapp.util.FakeStoryListPagingSource
import id.ergun.mystoryapp.util.MainDispatcherRule
import id.ergun.mystoryapp.util.noopListUpdateCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * @author erikgunawan
 * Created 04/12/22 at 00.38
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryListViewModelTest {

    private val testScope = TestScope()
    private val testDispatcher = StandardTestDispatcher(testScope.testScheduler)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var storyListUseCase: StoryListUseCase

    private lateinit var storyListViewModel: StoryListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        storyListViewModel = StoryListViewModel(storyListUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    @Test
    fun getStories() = runTest(testDispatcher) {
        val dummyStories = StoryDummy.generateDummyStories()
        val data = FakeStoryListPagingSource.snapshot(dummyStories)

        Mockito.`when`(storyListUseCase.getStories()).thenReturn(
            flow { emit(data) }
        )

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.Companion.DiffCallback,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        launch {
            storyListViewModel.getStories().collectLatest {
                differ.submitData(it)
                cancel()
            }

            Mockito.verify(storyListUseCase).getStories()
        }

        advanceUntilIdle()

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStories.size, differ.snapshot().size)
    }
}