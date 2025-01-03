package cz.tttie.qalculate.util

import cz.tttie.qalculate.binding.CalculatorFunction

data class CategoryTree(
    val name: String,
    val categories: MutableMap<String, CategoryTree> = mutableMapOf(),
    val functions: MutableList<CalculatorFunction> = mutableListOf()
)
