package cz.tttie.qalculate.binding

import android.content.Context
import com.getkeepsafe.relinker.ReLinker

class Qalculate(ctx: Context) : AutoCloseable {
    private val calculatorPtr: Long
    init {
        ReLinker.loadLibrary(ctx, "qalculate_binding")
        calculatorPtr = createCalculator()
    }

    /**
     * Calculates an expression, blocks until the operation is completed
     */
    external fun calculate(expr: String, darkTheme: Boolean = false): CalculationResult

    /**
     * Aborts the currently running calculation
     */
    external fun abortCalculation()

    /**
     * Polls whether a calculation or a print is pending
     */
    external fun isBusy(): Boolean

    companion object {
        @JvmStatic
        private external fun createCalculator(): Long
    }

    fun finalize() {
        close() // avoid leaking native resources when unused
    }

    override fun close() {
        deleteCalculator()
    }

    private external fun deleteCalculator()
}