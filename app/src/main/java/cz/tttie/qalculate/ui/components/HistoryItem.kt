package cz.tttie.qalculate.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cz.tttie.qalculate.db.HistoryEntry
import cz.tttie.qalculate.ui.LocalCalculator
import cz.tttie.qalculate.ui.vm.HistoryPageViewModel

@Composable
fun HistoryItem(
    entry: HistoryEntry,
    viewModel: HistoryPageViewModel,
    nav: NavHostController,
    modifier: Modifier = Modifier
) {
    val rootVm = LocalCalculator.current
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                viewModel.deleteEntry(entry)
                true
            } else {
                false
            }
        }
    )
    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.Settled -> MaterialTheme.colorScheme.surfaceDim
                    SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.surfaceDim
                }
            )

            val contentColor = contentColorFor(color)

            Box(Modifier
                .fillMaxSize()
                .background(color)) {
                Icon(
                    Icons.Rounded.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(8.dp),
                    tint = contentColor
                )
            }
        },
        modifier = modifier
    ) {
        OutlinedCard(shape = RectangleShape, onClick = {
            rootVm.appendToExpression(entry.result)
            nav.navigate("/") {
                popUpTo("/") { inclusive = true }
            }
        }) {
            ListItem(headlineContent = { Text(entry.expression) },
                supportingContent = { Text(entry.result) })
        }
    }
}