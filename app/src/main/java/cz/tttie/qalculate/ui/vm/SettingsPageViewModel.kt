package cz.tttie.qalculate.ui.vm

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import cz.tttie.qalculate.binding.Qalculate
import cz.tttie.qalculate.binding.options.evaluation.ApproximationMode
import cz.tttie.qalculate.binding.options.evaluation.UnitConversion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsPageViewModel(ctx: Context) : ViewModel() {
    private val prefs = ctx.getSharedPreferences("settings", Context.MODE_PRIVATE)
    private val _state = MutableStateFlow(
        SettingsPageState(
            approximationMode = ApproximationMode.entries[prefs.getInt("approximation", Qalculate.defaultEvaluationOptions.approximation.value)],
            precision = prefs.getInt("precision", Qalculate.defaultEvaluationOptions.precision),
            unitConversion = UnitConversion.entries[prefs.getInt("unitConversion", Qalculate.defaultEvaluationOptions.unitConversion.value)],
            colorizeExpressions = prefs.getBoolean("colorizeExpressions", false)
        )
    )

    val state = _state.asStateFlow()

    fun setApproximation (approximation: ApproximationMode) {
        prefs.edit(true) {
            putInt("approximation", approximation.value)
        }
        _state.update {
            it.copy(approximationMode = approximation)
        }
    }

    fun setPrecision (precision: Int) {
        prefs.edit(true) {
            putInt("precision", precision)
        }
        _state.update {
            it.copy(precision = precision)
        }
    }

    fun setUnitConversion (unitConversion: UnitConversion) {
        prefs.edit(true) {
            putInt("unitConversion", unitConversion.value)
        }
        _state.update {
            it.copy(unitConversion = unitConversion)
        }
    }

    fun setColorizeExpressions (colorize: Boolean) {
        prefs.edit(true) {
            putBoolean("colorizeExpressions", colorize)
        }
        _state.update {
            it.copy(colorizeExpressions = colorize)
        }
    }


    data class SettingsPageState(
        val approximationMode: ApproximationMode,
        val precision: Int,
        val unitConversion: UnitConversion,
        val colorizeExpressions: Boolean
    )

}