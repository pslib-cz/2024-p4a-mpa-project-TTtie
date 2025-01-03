package cz.tttie.qalculate.ui.vm

import androidx.lifecycle.ViewModel
import cz.tttie.qalculate.binding.CalculatorFunction
import cz.tttie.qalculate.util.CategoryTree
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CategoryViewModel(private val categoryTree: CategoryTree, val isRoot: Boolean) : ViewModel() {
    private var _state = MutableStateFlow(CategoryViewModelData(
        categoryLabels = buildList {
            if (categoryTree.functions.isNotEmpty() && categoryTree.categories.isNotEmpty()) {
                add("Functions")
            }
            addAll(categoryTree.categories.keys)
        },
        offset = if (categoryTree.functions.isNotEmpty()) 1 else 0,
        functions = categoryTree.functions,
        subcategory = if (categoryTree.functions.isEmpty()) categoryTree.categories.values.first() else null
    ))
    val state = _state.asStateFlow()

    fun updateSelected(index: Int) {
        _state.update {
            val subcategory = categoryTree.categories.values.elementAtOrNull(index - it.offset)
            it.copy(
                selectedIndex = index,
                subcategory = subcategory,
                functions = if (subcategory != null) emptyList() else categoryTree.functions
            )
        }
    }

    data class CategoryViewModelData(
        val categoryLabels: List<String> = emptyList(),
        val selectedIndex: Int = 0,
        val functions: List<CalculatorFunction> = emptyList(),
        val subcategory: CategoryTree? = null,
        internal val offset: Int = 0
    )
}