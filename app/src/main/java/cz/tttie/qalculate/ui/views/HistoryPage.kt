package cz.tttie.qalculate.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.tttie.qalculate.ui.theme.AppTypography
import cz.tttie.qalculate.ui.vm.HistoryPageViewModel

@Composable
fun HistoryPage(modifier: Modifier = Modifier) {
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
            LazyColumn(modifier = modifier) {
                items(state.history) { entry ->
                    Text(entry.expression)
                }
            }
        }
    }
}