package cz.tttie.qalculate.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {
    @Query("SELECT * FROM HistoryEntry ORDER BY id DESC")
    suspend fun getEntries(): List<HistoryEntry>

    @Insert
    suspend fun insertEntry(entry: HistoryEntry)

    @Delete
    suspend fun deleteEntry(entry: HistoryEntry)

    @Query("DELETE FROM HistoryEntry")
    suspend fun deleteAllEntries()
}
