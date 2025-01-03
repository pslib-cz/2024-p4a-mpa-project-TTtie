package cz.tttie.qalculate.ui.views

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cz.tttie.qalculate.binding.CalculatorFunction
import cz.tttie.qalculate.ui.LocalCalculator
import cz.tttie.qalculate.ui.vm.CategoryViewModel
import cz.tttie.qalculate.util.CategoryTree

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FunctionsPage(modifier: Modifier = Modifier) {
    val qalc = LocalCalculator.current
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
        CategoryViewModel(root, true)
    }

    var searching by rememberSaveable { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val searchBarPadding by animateDpAsState(
        if (searching) 0.dp else 16.dp, label = "searchBarPadding"
    )

    val stopSearching = {
        searching = false
        searchQuery = ""
    }

    Column(modifier = modifier) {
        val sbDefaults = SearchBarDefaults.colors()
        SearchBar(inputField = {
            SearchBarDefaults.InputField(searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = { searchQuery = it },
                placeholder = { Text("Search functions") },
                onExpandedChange = { searching = it },
                expanded = searching,
                leadingIcon = {
                    if (searching) {
                        IconButton(onClick = {
                            stopSearching()
                        }) {
                            Icon(
                                Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    } else {
                        Icon(
                            Icons.Rounded.Search, contentDescription = ""
                        )
                    }
                })
        },
            expanded = searching,
            onExpandedChange = { searching = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(searchBarPadding, 0.dp),
            colors = sbDefaults
        ) {
            if (searchQuery.isNotBlank()) {
                val results = qalc.functions.filter {
                    it.humanReadableName.contains(
                        searchQuery, ignoreCase = true
                    )
                }
                FunctionItemColumn(
                    results, colors = ListItemDefaults.colors(
                        containerColor = sbDefaults.containerColor
                    ), true
                )
            }
        }
        CategoryView(categorized)
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
                        key(name) {
                            Tab(selected = state.selectedIndex == index,
                                onClick = { vm.updateSelected(index) },
                                text = {
                                    Text(name)
                                })
                        }
                    }
                }, modifier = Modifier.fillMaxWidth())
            } else {
                SecondaryScrollableTabRow(state.selectedIndex, tabs = {
                    state.categoryLabels.forEachIndexed { index, name ->
                        key(name) {
                            Tab(selected = state.selectedIndex == index,
                                onClick = { vm.updateSelected(index) },
                                text = {
                                    Text(name)
                                })
                        }
                    }
                })
            }
        }

        if (state.functions.isNotEmpty()) {
            FunctionItemColumn(state.functions)
        } else if (state.subcategory != null) {
            CategoryView(
                CategoryViewModel(state.subcategory!!, false), modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun FunctionItemColumn(
    functions: List<CalculatorFunction>,
    colors: ListItemColors = ListItemDefaults.colors(),
    showCategory: Boolean = false,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(functions, key = { it.name }) {
            FunctionItem(it, colors, showCategory)
        }
    }
}

@Composable
fun FunctionItem(
    fn: CalculatorFunction,
    colors: ListItemColors,
    showCategory: Boolean,
    modifier: Modifier = Modifier
) {
    val qalc = LocalCalculator.current

    val sep = "${qalc.comma} "

    val nameWithArgs = buildString {
        append(fn.name)
        append("(")
        append(fn.arguments.joinToString(sep))
        append(")")
    }

    ListItem(headlineContent = {
        Text(fn.humanReadableName)
    }, supportingContent = {
        Text(fn.description)
    }, overlineContent = {
        Text(
            if (showCategory) "${
                fn.category.replace(
                    "/",
                    " ${Typography.rightGuillemet} "
                )
            } ${Typography.middleDot} $nameWithArgs" else nameWithArgs
        )
    }, modifier = modifier, colors = colors)
}