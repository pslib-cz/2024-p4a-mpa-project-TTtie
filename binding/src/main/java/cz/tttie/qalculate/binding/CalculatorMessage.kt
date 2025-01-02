package cz.tttie.qalculate.binding

data class CalculatorMessage(
    val type: MessageType,
    val message: String,
) {
    companion object {
        @JvmStatic
        private fun fromNative(type: Int, msg: String) = CalculatorMessage(
            MessageType.entries[type],
            msg
        )
    }
}
