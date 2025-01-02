package cz.tttie.qalculate.binding

data class CalculatorMessage(
    val type: MessageType,
    val message: String,
) {
    companion object {
        @JvmStatic
        private fun fromNative(type: Int, msg: String) = CalculatorMessage(
            when (type) {
                0 -> MessageType.INFORMATION
                1 -> MessageType.WARNING
                2 -> MessageType.ERROR
                else -> throw IllegalArgumentException("Invalid type argument")
            },
            msg
        )
    }
}
