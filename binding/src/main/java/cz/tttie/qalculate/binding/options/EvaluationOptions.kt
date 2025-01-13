package cz.tttie.qalculate.binding.options

import cz.tttie.qalculate.binding.options.evaluation.ApproximationMode
import cz.tttie.qalculate.binding.options.evaluation.UnitConversion

data class EvaluationOptions(
    val approximation: ApproximationMode,
    val precision: Int,
    val unitConversion: UnitConversion,
    val expressionColorization: Boolean
) {
    companion object {
        @JvmStatic
        private fun fromNative(
            approximation: Int,
            precision: Int,
            unitConversion: Int,
            expressionColorization: Boolean
        ) = EvaluationOptions(
            ApproximationMode.entries[approximation],
            precision,
            UnitConversion.entries[unitConversion],
            expressionColorization
        )
    }
}