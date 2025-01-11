package cz.tttie.qalculate.ui.vm

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
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
import kotlin.math.max

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

    fun onTextFieldValueUpdate(textFieldValue: TextFieldValue) {
        // Allow the user to modify the text only when keyboard mode is enabled
        _state.value = _state.value.copy(
            expression = if (_state.value.keyboardMode) textFieldValue
            else _state.value.expression.copy(text = _state.value.expression.text)
        )
    }

    fun appendToExpression(str: String) {
        _state.value = _state.value.copy(
            expression = _state.value.expression.copy(
                buildString {
                    append(_state.value.expression.text.take(_state.value.expression.selection.start))
                    append(str)
                    append(_state.value.expression.text.drop(_state.value.expression.selection.end))
                }, selection = TextRange(
                    _state.value.expression.selection.start + str.length,
                    _state.value.expression.selection.start + str.length
                )
            )
        )
    }

    fun appendToExpression(char: Char) = appendToExpression(char.toString())

    fun clearExpression() {
        _state.value = _state.value.copy(expression = TextFieldValue())
    }

    fun calculate() {
        val result = qalc.calculate(_state.value.expression.text, opts)
        _state.value = _state.value.copy(result = result)
    }

    fun backspace() {
        if (_state.value.expression.text.isNotEmpty()) _state.value = _state.value.copy(
            expression = _state.value.expression.copy(
                buildString {
                    append(
                        _state.value.expression.text.take(
                            max(
                                0, _state.value.expression.selection.start - 1
                            )
                        )
                    )
                    append(_state.value.expression.text.drop(_state.value.expression.selection.end))
                }, selection = TextRange(
                    max(0, _state.value.expression.selection.start - 1),
                    max(0, _state.value.expression.selection.start - 1)
                )
            )
        )
    }

    fun toggleKeyboardMode() {
        _state.value = _state.value.copy(keyboardMode = !_state.value.keyboardMode)
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
        val expression: TextFieldValue = TextFieldValue(),
        val result: CalculationResult? = null,
        val keyboardMode: Boolean = false,
    )
}