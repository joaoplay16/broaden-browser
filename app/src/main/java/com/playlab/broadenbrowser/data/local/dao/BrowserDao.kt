package com.playlab.broadenbrowser.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.playlab.broadenbrowser.model.Bookmark
import com.playlab.broadenbrowser.model.HistoryPage
import com.playlab.broadenbrowser.model.TabHistoryEntry
import com.playlab.broadenbrowser.model.TabPage
import kotlinx.coroutines.flow.Flow

@Dao
interface BrowserDao {

    // TABS

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

    // BROWSER HISTORY

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

    // TAB HISTORY

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTabHistoryEntry(tabHistoryEntry: TabHistoryEntry): Long

    @Query("""
        SELECT history.* FROM history 
        JOIN tab_history ON tab_history.historyPageId = history.id 
        JOIN tabs ON tab_history.tabId = tabs.id 
        WHERE tabs.id = :tabId ORDER BY tab_history.id
    """)
    fun getTabHistory(tabId: Long): Flow<List<HistoryPage>>

    @Query("""
        SELECT history.* FROM history 
        JOIN tab_history ON tab_history.historyPageId = history.id 
        JOIN tabs ON tab_history.tabId = tabs.id 
        WHERE tabs.id = :tabId ORDER BY timestamp DESC LIMIT 1
    """)
    suspend fun getLatestEntryFromTabHistory(tabId: Long): HistoryPage?

    @Query("DELETE FROM tab_history WHERE tabId = :tabId")
    suspend fun deleteTabHistory(tabId: Long): Int

    // BOOKMARKS

    @Query("SELECT * FROM bookmarks ORDER BY id")
    suspend fun getBookmarks(): List<Bookmark>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBookmark(bookmark: Bookmark): Long

    @Delete
    suspend fun deleteBookmarks(bookmark: List<Bookmark>)

    @Update
    suspend fun editBookmark(bookmark: Bookmark): Int

    @Query("SELECT * FROM bookmarks WHERE id = :id")
    suspend fun getBookmark(id: Long): Bookmark?
}