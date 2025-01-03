package cz.tttie.qalculate.binding.options.evaluation

enum class UnitConversion(val value: Int, val humanReadableName: String) {
    NONE(0, "None"),
    OPTIMAL_SI(1, "Convert to optimal SI units"),
    BASE(2, "Convert to base units"),
    OPTIMAL(3, "Convert to optimal units"),
}