package cz.tttie.qalculate.binding

class CalculationResult(
    val htmlResult: String,
    val messages: Array<CalculatorMessage>,
    val parsedExpression: String,
    val isApproximate: Boolean,
)