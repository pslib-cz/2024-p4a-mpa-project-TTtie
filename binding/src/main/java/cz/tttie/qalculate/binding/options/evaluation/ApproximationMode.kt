package cz.tttie.qalculate.binding.options.evaluation

enum class ApproximationMode(val value: Int, val humanReadableName: String) {
    EXACT(0, "Exact"),
    TRY_EXACT(1, "As exact as possible"),
    APPROXIMATE(2, "Approximate")
}