package com.playlab.broadenbrowser.domain

import com.playlab.broadenbrowser.model.TabPage
import com.playlab.broadenbrowser.repository.BrowserRepository
import javax.inject.Inject

class SaveEditTabUseCase @Inject constructor(
    private val browserRepository: BrowserRepository
) {
    suspend operator fun invoke(
        currentTabPage: TabPage?,
        tabPage: TabPage
    ): TabPage? {
        return if (currentTabPage == null || currentTabPage.id == 0 || tabPage.id == 0) {
            val id = browserRepository.insertTabPage(tabPage).toInt()
            if (id != -1) tabPage.copy(id = id) else null
        } else {
            val affectedRows = browserRepository.editTabPage(tabPage)
            if (affectedRows > 0) tabPage else null
        }
    }
}