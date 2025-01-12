package cz.tttie.qalculate.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {
    @Query("SELECT * FROM HistoryEntry ORDER BY timestamp DESC")
    suspend fun getHistoryEntries(): List<HistoryEntry>

    @Insert
    suspend fun insertHistoryEntry(entry: HistoryEntry)

    @Delete
    suspend fun deleteHistoryEntry(entry: HistoryEntry)
}
