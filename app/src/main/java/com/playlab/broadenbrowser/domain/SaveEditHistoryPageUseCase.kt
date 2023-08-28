package com.playlab.broadenbrowser.domain

import com.playlab.broadenbrowser.model.HistoryPage
import com.playlab.broadenbrowser.repository.BrowserRepository
import com.playlab.broadenbrowser.ui.utils.Util
import javax.inject.Inject

class SaveEditHistoryPageUseCase @Inject constructor(
    private val browserRepository: BrowserRepository
){
    suspend operator fun invoke(historyPage: HistoryPage): HistoryPage?{
        val existingHistoryPage = browserRepository
            .getTodayLatestHistoryPageByUrl(historyPage.url)

        if (existingHistoryPage != null && Util.isDateToday(existingHistoryPage.timestamp)) {
            browserRepository.editHistoryPage(
                historyPage = existingHistoryPage.copy(
                    timestamp = historyPage.timestamp
                )
            ).let {affectedRows ->
                if(affectedRows > 0) {
                    return existingHistoryPage.copy(
                        timestamp = historyPage.timestamp
                    )
                }
            }
        }else{
            return browserRepository.insertHistoryPage(historyPage).let {
                historyPage.copy(id = it.toInt())
            }
        }
        return null
    }
}