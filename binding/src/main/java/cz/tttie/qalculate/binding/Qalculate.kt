package cz.tttie.qalculate.binding

import android.content.Context
import android.util.Log
import com.getkeepsafe.relinker.ReLinker
import cz.tttie.qalculate.binding.options.EvaluationOptions

class Qalculate(ctx: Context) : AutoCloseable {
    private val assets = ctx.assets // used by native code
    private var closed = false
    private val calculatorPtr: Long
    init {
        ReLinker.loadLibrary(ctx, "qalculate_binding")
        calculatorPtr = createCalculator()
    }

    /**
     * Calculates an expression, blocks until the operation is completed
     */
    external fun calculate(expr: String, opts: EvaluationOptions = defaultEvaluationOptions): CalculationResult

    /**
     * Aborts the currently running calculation
     */
    external fun abortCalculation()

    /**
     * Polls whether a calculation or a print is pending
     */
    external fun isBusy(): Boolean

    val functions by lazy {
        getFunctionsNative()
    }

    val comma by lazy {
        getCommaNative()
    }

    private external fun getFunctionsNative(): Array<CalculatorFunction>
    external fun getVariables(opts: EvaluationOptions = defaultEvaluationOptions): Array<CalculatorVariable>

    companion object {
        val defaultEvaluationOptions by lazy {
            getDefaultEvaluationOptionsNative()
        }

        @JvmStatic
        private external fun createCalculator(): Long

        @JvmStatic
        private external fun getDefaultEvaluationOptionsNative(): EvaluationOptions
    }

    override fun close() {
        Log.d("Qalculate", "Closing calculator", Exception("Stack trace"))
        if (!closed) {
            deleteCalculator()
            closed = true
        }
    }

    private external fun deleteCalculator()

    private external fun getCommaNative(): String
}