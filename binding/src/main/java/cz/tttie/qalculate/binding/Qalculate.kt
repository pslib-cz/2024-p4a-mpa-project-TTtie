package cz.tttie.qalculate.binding

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import com.getkeepsafe.relinker.ReLinker
import cz.tttie.qalculate.binding.options.EvaluationOptions

class Qalculate private constructor(ctx: Context) : AutoCloseable {
    private val assets: AssetManager = ctx.assets
    private var closed = false
    init {
        ReLinker.loadLibrary(ctx, "qalculate_binding")
        createCalculator()
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
    external fun setPrecision(precision: Int)

    companion object {
        val defaultEvaluationOptions by lazy {
            getDefaultEvaluationOptionsNative()
        }

        @Volatile
        private var instance: Qalculate? = null

        fun getInstance(ctx: Context): Qalculate {
            return instance ?: synchronized(this) {
                instance ?: Qalculate(ctx).also { instance = it }
            }
        }

        @JvmStatic
        private external fun createCalculator()

        @JvmStatic
        private external fun getDefaultEvaluationOptionsNative(): EvaluationOptions
    }

    override fun close() {
        Log.d("Qalculate", "Closing calculator", Exception("Stack trace"))
        if (!closed) {
            deleteCalculator()
            closed = true
            instance = null
        }
    }

    private external fun deleteCalculator()

    private external fun getCommaNative(): String

    init {
        ReLinker.loadLibrary(ctx, "qalculate_binding")
    }
}