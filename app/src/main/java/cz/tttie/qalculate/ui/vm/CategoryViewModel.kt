package cz.tttie.qalculate.ui.vm

import androidx.lifecycle.ViewModel
import cz.tttie.qalculate.util.CategoryTree
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CategoryViewModel<T>(
    private val categoryTree: CategoryTree<T>,
    val isRoot: Boolean,
    val defaultCategoryName: String
) : ViewModel() {
    // Cache the subcategories to avoid recomputing them
    private val subcategories: List<CategoryViewModel<T>> = categoryTree.categories.values.map {
        CategoryViewModel(it, false, it.name)
    }

    private var _state = MutableStateFlow(CategoryViewModelData(
        categoryLabels = buildList {
            if (categoryTree.items.isNotEmpty() && categoryTree.categories.isNotEmpty()) {
                add(defaultCategoryName)
            }
            addAll(categoryTree.categories.keys)
        },
        offset = if (categoryTree.items.isNotEmpty()) 1 else 0,
        items = categoryTree.items,
        subcategory = if (categoryTree.items.isEmpty()) subcategories.first() else null
    ))
    val state = _state.asStateFlow()

    fun updateSelected(index: Int) {
        _state.update {
            val subcategory = subcategories.elementAtOrNull(index - it.offset)
            it.copy(
                selectedIndex = index,
                subcategory = subcategory,
                items = if (subcategory != null) emptyList() else categoryTree.items
            )
        }
    }

    data class CategoryViewModelData<T>(
        val categoryLabels: List<String> = emptyList(),
        val selectedIndex: Int = 0,
        val items: List<T> = emptyList(),
        val subcategory: CategoryViewModel<T>? = null,
        internal val offset: Int = 0
    )
}