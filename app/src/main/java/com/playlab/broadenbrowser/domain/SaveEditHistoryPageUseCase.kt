package com.playlab.broadenbrowser.domain

import com.playlab.broadenbrowser.model.HistoryPage
import com.playlab.broadenbrowser.repository.BrowserRepository
import com.playlab.broadenbrowser.ui.utils.Util
import javax.inject.Inject

class SaveEditHistoryPageUseCase @Inject constructor(
    private val browserRepository: BrowserRepository
){
    suspend operator fun invoke(historyPage: HistoryPage){
        val existingHistoryPage = browserRepository
            .getTodayLatestHistoryPageByUrl(historyPage.url)

        if (existingHistoryPage != null && Util.isDateToday(existingHistoryPage.timestamp)) {
            browserRepository.editHistoryPage(
                historyPage = existingHistoryPage.copy(
                    timestamp = historyPage.timestamp
                )
            )
        }else{
            browserRepository.insertHistoryPage(historyPage)
        }
    }
}