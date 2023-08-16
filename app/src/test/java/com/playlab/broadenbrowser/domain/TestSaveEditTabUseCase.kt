package com.playlab.broadenbrowser.domain

import com.google.common.truth.Truth.assertThat
import com.playlab.broadenbrowser.MainCoroutineRule
import com.playlab.broadenbrowser.mocks.MockTabPages.tab1
import com.playlab.broadenbrowser.mocks.MockTabPages.tab2
import com.playlab.broadenbrowser.mocks.MockTabPages.tab3
import com.playlab.broadenbrowser.mocks.MockTabPages.tab4
import com.playlab.broadenbrowser.repository.FakeBrowserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TestSaveEditTabUseCase {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var saveEditTabUseCase: SaveEditTabUseCase
    private lateinit var fakeBrowserRepository: FakeBrowserRepository

    @Before
    fun before() = runTest {
        fakeBrowserRepository = FakeBrowserRepository()
        saveEditTabUseCase = SaveEditTabUseCase(fakeBrowserRepository)
    }

    @Test
    fun `save a tab page when currentTab is null or id is equal to 0`() =
        runTest {

            val result1 = saveEditTabUseCase(
                currentTabPage = null,
                tabPage = tab2
            )
            val result2 = saveEditTabUseCase(
                currentTabPage = tab1.copy(id = 0),
                tabPage = tab3
            )

            assertThat(result1).isNotNull()
            assertThat(result2).isNotNull()
        }


    @Test
    fun `edit a tab page when currentTab is not null or id is not 0`() =
        runTest {
            val id = fakeBrowserRepository.insertTabPage(tab4)
            val currentTab = tab4.copy(id = id.toInt())
            val newTitle = "Web Page"

            val result = saveEditTabUseCase(
                currentTabPage = currentTab,
                tabPage = currentTab.copy(
                    title = newTitle,
                    timestamp = System.currentTimeMillis()
                )
            )

            assertThat(result).isNotNull()
            assertThat(result?.title).isEqualTo(newTitle)
            assertThat(result?.timestamp).isGreaterThan(currentTab.timestamp)
        }
}