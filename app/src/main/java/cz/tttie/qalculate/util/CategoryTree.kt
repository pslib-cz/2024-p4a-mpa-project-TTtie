package cz.tttie.qalculate.util

import java.util.SortedMap

data class CategoryTree<T>(
    val name: String,
    val categories: SortedMap<String, CategoryTree<T>> = sortedMapOf(),
    val items: MutableList<T> = mutableListOf()
)
