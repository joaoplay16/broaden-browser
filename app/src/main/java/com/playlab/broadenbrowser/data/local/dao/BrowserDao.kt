package com.playlab.broadenbrowser.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.playlab.broadenbrowser.model.TabPage
import kotlinx.coroutines.flow.Flow

@Dao
interface BrowserDao {
    @Query("SELECT * FROM tabs")
    fun getTabPages(): Flow<List<TabPage>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTabPage(tabPage: TabPage): Long

    @Delete
    suspend fun deleteTabPages(tabPages: List<TabPage>)

    @Query("DELETE FROM tabs")
    suspend fun deleteAllTabPages()
}