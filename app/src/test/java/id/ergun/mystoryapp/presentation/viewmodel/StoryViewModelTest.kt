package id.ergun.mystoryapp.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.domain.usecase.story.StoryUseCase
import id.ergun.mystoryapp.presentation.model.StoryDummy
import id.ergun.mystoryapp.util.MainDispatcherRule
import id.ergun.mystoryapp.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
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
class StoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var storyUseCase: StoryUseCase

    private lateinit var storyViewModel: StoryViewModel

    @Before
    fun setup() {
        storyViewModel = StoryViewModel(storyUseCase)
    }

    @Test
    fun `when Create Story Should Not Null And Return Success`() = runTest {
        val request = StoryDummy.getDummyStoryCreateRequest()

        Mockito.`when`(storyUseCase.createStory(request))
            .thenReturn(StoryDummy.getDummyStoryCreated())

        storyViewModel.createStory(request)
        val actualResponse = storyViewModel.createStoryResponse.getOrAwaitValue()

        Mockito.verify(storyUseCase).createStory(request)

        Assert.assertTrue(actualResponse.status == ResponseWrapper.Status.SUCCESS)
    }

    @Test
    fun `when Create Story Should Return Error`() = runTest {
        val request = StoryDummy.getDummyStoryCreateRequest()

        Mockito.`when`(storyUseCase.createStory(request))
            .thenReturn(flow {
                emit(ResponseWrapper.error("Error"))
            })

        storyViewModel.createStory(request)
        val actualResponse = storyViewModel.createStoryResponse.getOrAwaitValue()

        Mockito.verify(storyUseCase).createStory(request)

        Assert.assertTrue(actualResponse.status == ResponseWrapper.Status.ERROR)
    }

    @Test
    fun `when Get Story List Should Not Null And Return Success`() = runTest {
        Mockito.`when`(storyUseCase.getStories(StoryDummy.getStoryParams()))
            .thenReturn(StoryDummy.getDummyStories())

        storyViewModel.getStories(StoryDummy.getStoryParams())
        val actualStories = storyViewModel.stories.getOrAwaitValue()

        Mockito.verify(storyUseCase).getStories(StoryDummy.getStoryParams())

        Assert.assertNotNull(actualStories.data)
        Assert.assertTrue(actualStories.status == ResponseWrapper.Status.SUCCESS)
        Assert.assertEquals(actualStories.data?.size, StoryDummy.generateDummyStories().size)
    }

    @Test
    fun `when Get Story List Should Null And Return Error`() = runTest {
        Mockito.`when`(storyUseCase.getStories(StoryDummy.getStoryParams()))
            .thenReturn(flow {
                emit(ResponseWrapper.error("Error"))
            })

        storyViewModel.getStories(StoryDummy.getStoryParams())
        val actualStories = storyViewModel.stories.getOrAwaitValue()

        Mockito.verify(storyUseCase).getStories(StoryDummy.getStoryParams())

        Assert.assertNull(actualStories.data)
        Assert.assertTrue(actualStories.status == ResponseWrapper.Status.ERROR)
    }
}