@file:OptIn(ExperimentalMaterial3Api::class)

package cz.tttie.qalculate.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cz.tttie.qalculate.ui.vm.CategoryViewModel

typealias ItemRenderer<T> = @Composable LazyItemScope.(T, Boolean) -> Unit

@Composable
fun <T> CategorizedListing(
    categoryTree: CategoryViewModel<T>,
    searchPlaceholderText: String,
    search: (String) -> List<T>,
    keyProvider: ((T) -> Any)? = null,
    modifier: Modifier = Modifier,
    itemRenderer: ItemRenderer<T>,
) {

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
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = { searchQuery = it },
                    placeholder = { Text(searchPlaceholderText) },
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
                .consumeWindowInsets(WindowInsets.statusBars)
                .fillMaxWidth()
                .padding(searchBarPadding, 0.dp),
            colors = sbDefaults
        ) {
            if (searchQuery.isNotBlank()) {
                val results = search(searchQuery)
                FunctionItemColumn(
                    results, searching = true, itemRenderer = itemRenderer
                )
            }
        }
        CategoryView(categoryTree, itemRenderer = itemRenderer, keyProvider = keyProvider)
    }

}

@Composable
fun <T> CategoryView(
    vm: CategoryViewModel<T>,
    keyProvider: ((T) -> Any)? = null,
    modifier: Modifier = Modifier,
    itemRenderer: ItemRenderer<T>
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

        if (state.items.isNotEmpty()) {
            FunctionItemColumn(
                state.items,
                keyProvider = keyProvider,
                searching = false,
                itemRenderer = itemRenderer
            )
        } else if (state.subcategory != null) {
            CategoryView(
                state.subcategory!!,
                modifier = Modifier.fillMaxWidth(),
                keyProvider = keyProvider,
                itemRenderer = itemRenderer
            )
        }
    }
}

@Composable
fun <T> FunctionItemColumn(
    functions: List<T>,
    keyProvider: ((T) -> Any)? = null,
    searching: Boolean,
    modifier: Modifier = Modifier,
    itemRenderer: ItemRenderer<T>
) {
    LazyColumn(modifier = modifier) {
        items(functions, key = keyProvider) {
            itemRenderer(it, searching)
        }
    }
}