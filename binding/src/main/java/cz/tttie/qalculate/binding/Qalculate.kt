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
     * A native method that is implemented by the 'binding' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

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