package cz.tttie.qalculate.ui.views

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import cz.tttie.qalculate.binding.CalculatorVariable
import cz.tttie.qalculate.ui.LocalCalculator
import cz.tttie.qalculate.ui.components.CategorizedListing
import cz.tttie.qalculate.ui.vm.CategoryViewModel
import cz.tttie.qalculate.util.CategoryTree

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VariablesPage(modifier: Modifier = Modifier) {
    val rootVm = LocalCalculator.current
    val vars = remember { rootVm.useQalc { it.getVariables(rootVm.opts) } }
    val categorized = remember {
        val root = CategoryTree<CalculatorVariable>("Root")

        vars.forEach { fn ->
            val parts = fn.category.split("/")
            var current = root
            // Create the category tree
            parts.forEach { part ->
                current = current.categories.getOrPut(part) { CategoryTree(part) }
            }
            current.items.add(fn)
        }
        CategoryViewModel(root, true, "Variables")
    }

    val sbDefaults = SearchBarDefaults.colors()

    val searchColors = ListItemDefaults.colors(
        containerColor = sbDefaults.containerColor
    )
    val listItemColors = ListItemDefaults.colors()

    CategorizedListing(categorized, "Search variables", { q ->
        vars.filter { it.humanReadableName.contains(q, ignoreCase = true) }
    }, { it.name }, modifier) { item, searching ->
        VariableItem(
            item,
            if (searching) searchColors else listItemColors,
            showCategory = searching
        )
    }

}

@Composable
fun VariableItem(
    fn: CalculatorVariable,
    colors: ListItemColors,
    showCategory: Boolean,
    modifier: Modifier = Modifier
) {
    ListItem(headlineContent = {
        Text(fn.humanReadableName)
    }, supportingContent = {
        Text(AnnotatedString.fromHtml(fn.description))
    }, overlineContent = {
        Text(
            if (showCategory) "${
                fn.category.replace(
                    "/",
                    " ${Typography.rightGuillemet} "
                )
            } ${Typography.middleDot} ${fn.name}" else fn.name
        )
    }, modifier = modifier, colors = colors)
}