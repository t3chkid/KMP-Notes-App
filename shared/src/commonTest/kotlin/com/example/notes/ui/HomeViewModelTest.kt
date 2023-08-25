package com.example.notes.ui

import com.example.notes.data.NotesRepository
import com.example.notes.domain.Note
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var homeViewModel: HomeViewModel
    private val testScope = TestScope(context = StandardTestDispatcher())
    private val savedNotes = List(10) {
        Note(
            id = it.toLong(),
            title = "Note #$it",
            content = "Note #$it",
            isDeleted = false
        )
    }

    @BeforeTest
    fun setup() {
        val notesRepositoryMock = mockk<NotesRepository>()
        every { notesRepositoryMock.savedNotesStream } returns flowOf(savedNotes)

        homeViewModel = HomeViewModel(
            notesRepository = notesRepositoryMock,
            viewModelScope = testScope
        )
    }

    @Test
    fun `searchNotesTest - Notes with titles containing the search text much be fetched successfully`() =
        testScope.runTest {
            homeViewModel.search("Note #1")
            advanceUntilIdle()
            assertTrue {
                val expectedNoteToBeReturned = savedNotes.find { it.title == "Note #1" }
                val expectedNoteListToBeReturned = listOf(expectedNoteToBeReturned)
                homeViewModel.uiState.value.searchResults.containsAll(expectedNoteListToBeReturned)
            }

            homeViewModel.search("Note #2")
            advanceUntilIdle()
            assertTrue {
                val expectedNoteToBeReturned = savedNotes.find { it.title == "Note #2" }
                val expectedNoteListToBeReturned = listOf(expectedNoteToBeReturned)
                homeViewModel.uiState.value.searchResults.containsAll(expectedNoteListToBeReturned)
            }
        }

    @Test
    fun `searchNotesTest - Notes with contents containing the search text much be fetched successfully`() =
        testScope.runTest {
            homeViewModel.search("Note #1")
            advanceUntilIdle()
            assertTrue {
                val expectedNoteToBeReturned = savedNotes.find { it.content == "Note #1" }
                val expectedNoteListToBeReturned = listOf(expectedNoteToBeReturned)
                homeViewModel.uiState.value.searchResults.containsAll(expectedNoteListToBeReturned)
            }

            homeViewModel.search("Note #2")
            advanceUntilIdle()
            assertTrue {
                val expectedNoteToBeReturned = savedNotes.find { it.content == "Note #2" }
                val expectedNoteListToBeReturned = listOf(expectedNoteToBeReturned)
                homeViewModel.uiState.value.searchResults.containsAll(expectedNoteListToBeReturned)
            }
        }

}