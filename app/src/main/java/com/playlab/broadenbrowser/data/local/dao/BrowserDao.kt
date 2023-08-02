package com.playlab.broadenbrowser.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.playlab.broadenbrowser.model.HistoryPage
import com.playlab.broadenbrowser.model.TabPage
import kotlinx.coroutines.flow.Flow

@Dao
interface BrowserDao {
    @Query("SELECT * FROM tabs ORDER BY timestamp DESC")
    fun getTabPages(): Flow<List<TabPage>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTabPage(tabPage: TabPage): Long

    @Delete
    suspend fun deleteTabPages(tabPages: List<TabPage>)

    @Query("DELETE FROM tabs")
    suspend fun deleteAllTabPages()

    @Update
    suspend fun editTabPage(tabPage: TabPage): Int

    @Query("SELECT * FROM tabs WHERE id = :id")
    suspend fun getTab(id: Long): TabPage?

    @Query("SELECT * FROM history ORDER BY timestamp DESC")
    fun getHistory(): Flow<List<HistoryPage>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHistoryPage(historyPage: HistoryPage): Long

    @Delete
    suspend fun deleteHistoryPages(historyPages: List<HistoryPage>)

    @Query("DELETE FROM history")
    suspend fun deleteAllHistoryPages()

    @Update
    suspend fun editHistoryPage(tabPage: HistoryPage): Int

    @Query("SELECT * FROM history WHERE id = :id")
    suspend fun getHistoryPage(id: Long): HistoryPage?

    @Query(
        """SELECT * FROM history 
            WHERE url = :url
            and date(timestamp/1000, 'unixepoch', 'localtime') = date('now', 'localtime') 
            ORDER by timestamp desc LIMIT 1"""
    )
    suspend fun getTodayLatestHistoryPageByUrl(url: String): HistoryPage?
}