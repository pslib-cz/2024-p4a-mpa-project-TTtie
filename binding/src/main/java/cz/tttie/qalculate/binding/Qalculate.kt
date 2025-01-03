package cz.tttie.qalculate.binding

import android.content.Context
import com.getkeepsafe.relinker.ReLinker
import cz.tttie.qalculate.binding.options.EvaluationOptions

class Qalculate(ctx: Context) : AutoCloseable {
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
        getFns()
    }

    private external fun getFns(): Array<CalculatorFunction>

    companion object {
        val defaultEvaluationOptions by lazy {
            getDefaultEvaluationOptionsNative()
        }

        @JvmStatic
        private external fun createCalculator(): Long

        @JvmStatic
        private external fun getDefaultEvaluationOptionsNative(): EvaluationOptions
    }

    fun finalize() {
        close() // avoid leaking native resources when unused
    }

    override fun close() {
        deleteCalculator()
    }

    private external fun deleteCalculator()
}