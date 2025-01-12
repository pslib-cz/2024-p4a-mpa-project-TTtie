package cz.tttie.qalculate.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.tttie.qalculate.QalcApplication
import cz.tttie.qalculate.db.HistoryEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryPageViewModel : ViewModel() {
    private val _state = MutableStateFlow(HistoryPageState())
    val state = _state.asStateFlow()


    fun load() {
        viewModelScope.launch {
            try {
                val history = QalcApplication.database.historyDao().getHistoryEntries()
                _state.update { it.copy(history = history, loading = false) }
            } catch (e: Throwable) {
                _state.update {
                    it.copy(error = e, loading = false)
                }
            }
        }
    }


    data class HistoryPageState(
        val history: List<HistoryEntry> = emptyList(),
        val loading: Boolean = true,
        val error: Throwable? = null
    )
}