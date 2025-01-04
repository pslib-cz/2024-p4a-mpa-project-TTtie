package cz.tttie.qalculate.util

data class CategoryTree<T>(
    val name: String,
    val categories: MutableMap<String, CategoryTree<T>> = mutableMapOf(),
    val items: MutableList<T> = mutableListOf()
)
