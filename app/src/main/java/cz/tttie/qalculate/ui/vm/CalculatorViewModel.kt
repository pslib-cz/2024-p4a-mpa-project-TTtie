package cz.tttie.qalculate.ui.vm

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import cz.tttie.qalculate.binding.CalculationResult
import cz.tttie.qalculate.binding.Qalculate
import cz.tttie.qalculate.binding.options.EvaluationOptions
import cz.tttie.qalculate.binding.options.evaluation.ApproximationMode
import cz.tttie.qalculate.binding.options.evaluation.UnitConversion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class CalculatorViewModel(ctx: Context, private val qalc: Qalculate) : ViewModel() {
    private val qalcLock = ReentrantLock()
    private val prefs: SharedPreferences =
        ctx.getSharedPreferences("settings", Context.MODE_PRIVATE)
    var opts = loadOptsFromPreferences()

    init {
        qalc.setPrecision(opts.precision)
    }

    /**
     * Grabs a lock to the {@see qalc} instance and executes the given function.
     */
    fun <R> useQalc(fn: (Qalculate) -> R): R = qalcLock.withLock {
        fn(qalc)
    }

    private val _state = MutableStateFlow(CalculatorState())
    val state = _state.asStateFlow()

    fun appendToExpression(char: Char) {
        _state.value = _state.value.copy(expression = _state.value.expression + char)
    }

    fun clearExpression() {
        _state.value = _state.value.copy(expression = "")
    }

    fun calculate() {
        val result = qalc.calculate(_state.value.expression)
        _state.value = _state.value.copy(result = result)
    }

    fun backspace() {
        _state.value = _state.value.copy(expression = _state.value.expression.dropLast(1))
    }

    private fun loadOptsFromPreferences() = EvaluationOptions(
        approximation = ApproximationMode.entries[prefs.getInt(
            "approximation", Qalculate.defaultEvaluationOptions.approximation.value
        )],
        precision = prefs.getInt("precision", Qalculate.defaultEvaluationOptions.precision),
        unitConversion = UnitConversion.entries[prefs.getInt(
            "unitConversion", Qalculate.defaultEvaluationOptions.unitConversion.value
        )],
        expressionColorization = prefs.getBoolean(
            "colorizeExpressions", Qalculate.defaultEvaluationOptions.expressionColorization
        )
    )

    data class CalculatorState(
        val expression: String = "", val result: CalculationResult? = null
    )
}