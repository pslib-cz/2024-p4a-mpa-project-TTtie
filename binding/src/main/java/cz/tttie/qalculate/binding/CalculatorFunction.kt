package cz.tttie.qalculate.binding

data class CalculatorFunction(
    val name: String,
    val humanReadableName: String,
    val description: String,
    val category: String,
    val arguments: List<Pair<String, String>>
) {
    companion object {
        @JvmStatic
        private fun fromNative(
            name: String,
            humanReadableName: String,
            description: String,
            category: String,
            arguments: Array<Array<String>>
        ) = CalculatorFunction(
            name,
            humanReadableName,
            description,
            category,
            arguments.map { it[0] to it[1] }
        )
    }
}
