package cz.tttie.qalculate.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import cz.tttie.qalculate.binding.CalculatorFunction
import cz.tttie.qalculate.ui.LocalCalculator
import cz.tttie.qalculate.ui.components.CategorizedListing
import cz.tttie.qalculate.ui.vm.CategoryViewModel
import cz.tttie.qalculate.util.CategoryTree

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FunctionsPage(nav: NavHostController, modifier: Modifier = Modifier) {
    val rootVm = LocalCalculator.current
    val fns = remember {
        rootVm.useQalc {
            it.functions
        }.sortedBy { it.name }
    }
    val categorized = viewModel<CategoryViewModel<CalculatorFunction>>(factory = viewModelFactory {
        addInitializer(CategoryViewModel::class) {
            val root = CategoryTree<CalculatorFunction>("Root")

            fns.forEach { fn ->
                val parts = fn.category.split("/")
                var current = root
                // Create the category tree
                parts.forEach { part ->
                    current = current.categories.getOrPut(part) { CategoryTree(part) }
                }
                current.items.add(fn)
            }
            CategoryViewModel(root, true, "Functions")
        }
    })

    val sbDefaults = SearchBarDefaults.colors()

    val searchColors = ListItemDefaults.colors(
        containerColor = sbDefaults.containerColor
    )
    val listItemColors = ListItemDefaults.colors()

    CategorizedListing(categorized, "Search functions", { q ->
        fns.filter { it.humanReadableName.contains(q, ignoreCase = true) }
    }, { it.name }, modifier) { item, searching ->
        FunctionItem(
            nav, item, if (searching) searchColors else listItemColors, showCategory = searching
        )
    }

}

@Composable
fun FunctionItem(
    nav: NavHostController,
    fn: CalculatorFunction,
    colors: ListItemColors,
    showCategory: Boolean,
    modifier: Modifier = Modifier
) {
    val rootVm = LocalCalculator.current
    val sep = remember { "${rootVm.useQalc { it.comma }} " }

    val nameWithArgs = buildString {
        append(fn.name)
        append("(")
        append(fn.arguments.joinToString(sep) { if (it.second.isNotBlank()) "${it.first}: ${it.second}" else it.first })
        append(")")
    }

    val typelessArgs = fn.arguments.joinToString(sep) { it.first }

    ListItem(headlineContent = {
        Text(fn.humanReadableName)
    }, supportingContent = {
        Text(fn.description)
    }, overlineContent = {
        Text(
            if (showCategory) "${
                fn.category.replace(
                    "/", " ${Typography.rightGuillemet} "
                )
            } ${Typography.middleDot} $nameWithArgs" else nameWithArgs
        )
    }, modifier = modifier.clickable {
        rootVm.appendToExpression("${fn.name}($typelessArgs)")
        nav.navigate("/") {
            popUpTo("/") {}
        }
    }, colors = colors)
}