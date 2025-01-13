package cz.tttie.qalculate.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntry(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val expression: String,
    val result: String
)
