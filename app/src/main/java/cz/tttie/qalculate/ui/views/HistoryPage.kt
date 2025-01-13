package cz.tttie.qalculate.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import cz.tttie.qalculate.ui.components.HistoryItem
import cz.tttie.qalculate.ui.theme.AppTypography
import cz.tttie.qalculate.ui.vm.HistoryPageViewModel

@Composable
fun HistoryPage(nav: NavHostController, modifier: Modifier = Modifier) {
    val vm = viewModel<HistoryPageViewModel>()
    val state by vm.state.collectAsState()

    LaunchedEffect(true) {
        vm.load()
    }

    when {
        state.loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(48.dp))
            }
        }

        state.error != null -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(":(", style = AppTypography.displayLarge)
                Text("The history failed to load", style = AppTypography.bodyLarge)
            }
        }

        state.history.isEmpty() -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(":(", style = AppTypography.displayLarge)
                Text("No history yet, try calculating something", style = AppTypography.bodyLarge)
            }
        }

        else -> {
            Column(modifier) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(state.history, key = {it.id}) { entry ->
                        HistoryItem(entry, vm, nav)
                    }
                }
                BottomAppBar(actions = {
                    IconButton({
                        vm.deleteAllEntries()
                    }) {
                        Icon(Icons.Rounded.Delete, contentDescription = "Delete all")
                    }
                }, Modifier.consumeWindowInsets(WindowInsets.navigationBars))
            }

        }
    }
}