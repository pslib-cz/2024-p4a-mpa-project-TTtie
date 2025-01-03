package cz.tttie.qalculate.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cz.tttie.qalculate.binding.CalculatorFunction
import cz.tttie.qalculate.binding.Qalculate
import cz.tttie.qalculate.ui.vm.CategoryViewModel
import cz.tttie.qalculate.util.CategoryTree

@Composable
fun FunctionsPage(qalc: Qalculate, modifier: Modifier = Modifier) {
    val categorized = remember {
        val root = CategoryTree("Root")

        qalc.functions.forEach { fn ->
            val parts = fn.category.split("/")
            var current = root
            // Create the category tree
            parts.forEach { part ->
                current = current.categories.getOrPut(part) { CategoryTree(part) }
            }
            current.functions.add(fn)
        }
        root
    }

    Column(modifier = modifier) {
        CategoryView(CategoryViewModel(categorized, true))
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryView(
    vm: CategoryViewModel, modifier: Modifier = Modifier
) {
    val state by vm.state.collectAsState()

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        if (state.categoryLabels.isNotEmpty()) {
            if (vm.isRoot) {
                PrimaryScrollableTabRow(state.selectedIndex, tabs = {
                    state.categoryLabels.forEachIndexed { index, name ->
                        Tab(selected = state.selectedIndex == index,
                            onClick = { vm.updateSelected(index) },
                            text = {
                                Text(name)
                            })
                    }
                }, modifier = Modifier.fillMaxWidth())
            } else {
                SecondaryScrollableTabRow(state.selectedIndex, tabs = {
                    state.categoryLabels.forEachIndexed { index, name ->
                        Tab(selected = state.selectedIndex == index,
                            onClick = { vm.updateSelected(index) },
                            text = {
                                Text(name)
                            })
                    }
                })
            }
        }

        if (state.functions.isNotEmpty()) {
            FunctionItemColumn(state.functions)
        } else if (state.subcategory != null) {
            CategoryView(
                CategoryViewModel(state.subcategory!!, false),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun FunctionItemColumn(functions: List<CalculatorFunction>, modifier: Modifier = Modifier) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier) {
        items(functions) {
            FunctionItem(it)
        }
    }
}

@Composable
fun FunctionItem(fn: CalculatorFunction, modifier: Modifier = Modifier) {
    ListItem(headlineContent = {
        Text(
            fn.humanReadableName,
        )
    }, supportingContent = {
        Text(
            fn.description,
        )
    }, overlineContent = {
        Text(
            fn.name,
        )
    }, modifier = modifier
    )
}