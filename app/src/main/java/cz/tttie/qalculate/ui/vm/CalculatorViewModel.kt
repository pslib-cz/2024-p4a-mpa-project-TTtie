package cz.tttie.qalculate.ui.vm

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.tttie.qalculate.QalcApplication
import cz.tttie.qalculate.binding.CalculationResult
import cz.tttie.qalculate.binding.Qalculate
import cz.tttie.qalculate.binding.options.EvaluationOptions
import cz.tttie.qalculate.binding.options.evaluation.ApproximationMode
import cz.tttie.qalculate.binding.options.evaluation.UnitConversion
import cz.tttie.qalculate.db.HistoryEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        _state.update {
            it.copy(
                expression = if (it.keyboardMode) textFieldValue
                else it.expression.copy(text = it.expression.text)
            )
        }
    }

    fun appendToExpression(str: String) {
        _state.update {
            it.copy(
                expression = it.expression.copy(
                    buildString {
                        append(it.expression.text.take(it.expression.selection.start))
                        append(str)
                        append(it.expression.text.drop(it.expression.selection.end))
                    }, selection = TextRange(
                        it.expression.selection.start + str.length,
                        it.expression.selection.start + str.length
                    )
                )
            )
        }
    }

    fun appendToExpression(char: Char) = appendToExpression(char.toString())

    fun clearExpression() {
        _state.update { it.copy(expression = TextFieldValue()) }
    }

    fun calculate() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.Default) {
                useQalc { qalc ->
                    qalc.calculate(_state.value.expression.text, opts)
                }
            }

            QalcApplication.database.historyDao().insertEntry(
                HistoryEntry(
                    0L, dehtmlize(result.parsedExpression), dehtmlize(result.htmlResult)
                )
            )
            _state.update { it.copy(result = result) }
        }
    }

    fun backspace() {
        if (_state.value.expression.text.isNotEmpty()) _state.update {
            it.copy(
                expression = it.expression.copy(
                    buildString {
                        append(it.expression.text.take(it.expression.selection.start - 1))
                        append(it.expression.text.drop(it.expression.selection.end))
                    }, selection = TextRange(
                        max(0, it.expression.selection.start - 1),
                        max(0, it.expression.selection.start - 1)
                    )
                )
            )
        }
    }

    fun toggleKeyboardMode() {
        _state.update { it.copy(keyboardMode = !it.keyboardMode) }
    }

    fun appendLastAnswer() {
        viewModelScope.launch {
            val lastEntry = QalcApplication.database.historyDao().getLastEntry() ?: return@launch

            appendToExpression(lastEntry.result)
        }
    }

    private fun dehtmlize(data: String) = AnnotatedString.fromHtml(data).toString()

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