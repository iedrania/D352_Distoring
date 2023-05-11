package com.iedrania.distoring.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.*
import androidx.recyclerview.widget.ListUpdateCallback
import com.iedrania.distoring.data.StoryRepository
import com.iedrania.distoring.ui.main.StoryAdapter
import com.iedrania.distoring.utils.DataDummy
import junit.framework.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class StoryViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var storyViewModel: StoryViewModel

    @Mock
    private lateinit var storyRepository: StoryRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val dummyStory = DataDummy.generateDummyStory()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getStorySuccessTest() = runTest {
        val expectedStory = flow {
            emit(PagingData.from(dummyStory))
        }

        `when`(storyRepository.getStory()).thenAnswer {
            expectedStory
        }

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = testDispatcher,
            workerDispatcher = testDispatcher,
        )

        storyViewModel = StoryViewModel(storyRepository)

        val job = launch {
            storyViewModel.story.collectLatest { pagingData ->
                differ.submitData(pagingData)
            }
        }

        advanceUntilIdle()

        // Memastikan data tidak null.
        assertNotNull(differ.snapshot())

        // Memastikan jumlah data sesuai dengan yang diharapkan.
        assertEquals(dummyStory.size, differ.snapshot().size)

        // Memastikan data pertama yang dikembalikan sesuai.
        assertEquals(dummyStory.first(), differ.snapshot()[0])

        job.cancel()
    }

    @Test
    fun getStoryNoStoryTest() = runTest {
        val expectedStory = flow {
            emit(PagingData.from(ArrayList()))
        }

        `when`(storyRepository.getStory()).thenAnswer {
            expectedStory
        }

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = testDispatcher,
            workerDispatcher = testDispatcher,
        )

        storyViewModel = StoryViewModel(storyRepository)

        val job = launch {
            storyViewModel.story.collectLatest { pagingData ->
                differ.submitData(pagingData)
            }
        }

        advanceUntilIdle()

        // Memastikan jumlah data yang dikembalikan nol.
        assertEquals(0, differ.snapshot().size)

        job.cancel()
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}