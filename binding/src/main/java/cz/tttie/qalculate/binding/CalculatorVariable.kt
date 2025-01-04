package cz.tttie.qalculate.binding

data class CalculatorVariable(
    val name: String,
    val humanReadableName: String,
    val description: String,
    val category: String,
) {
    companion object {
        @JvmStatic
        private fun fromNative(
            name: String,
            humanReadableName: String,
            description: String,
            category: String,
        ) = CalculatorVariable(
            name,
            humanReadableName,
            description,
            category,
        )
    }
}
