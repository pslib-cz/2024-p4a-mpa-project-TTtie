package cz.tttie.qalculate.ui.views

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Calculate
import androidx.compose.material.icons.rounded.FormatColorText
import androidx.compose.material.icons.rounded.SyncAlt
import androidx.compose.material.icons.rounded._123
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.tttie.qalculate.binding.options.evaluation.ApproximationMode
import cz.tttie.qalculate.binding.options.evaluation.UnitConversion
import cz.tttie.qalculate.ui.theme.AppTypography
import cz.tttie.qalculate.ui.vm.SettingsPageViewModel
import kotlin.math.max

@Composable
fun SettingsPage(vm: SettingsPageViewModel = viewModel(), modifier: Modifier = Modifier) {
    val state by vm.state.collectAsState()

    Column(modifier.verticalScroll(rememberScrollState())) {
        EnumListItem(selectedOption = state.approximationMode,
            options = ApproximationMode.entries.toTypedArray(),
            onOptionSelected = {
                vm.setApproximation(it)
            },
            title = "Approximation mode",
            icon = Icons.Rounded.Calculate,
            labelProvider = { it.humanReadableName })
        NumberListItem(value = state.precision,
            onValueChanged = {
                vm.setPrecision(it)
            },
            title = "Precision",
            icon = Icons.Rounded._123,
            supportingContent = {
                if (it <= -1) {
                    Text("Unlimited")
                } else {
                    Text("$it digits")
                }
            })
        EnumListItem(selectedOption = state.unitConversion,
            options = UnitConversion.entries.toTypedArray(),
            onOptionSelected = {
                vm.setUnitConversion(it)
            },
            title = "Unit conversion",
            icon = Icons.Rounded.SyncAlt,
            labelProvider = { it.humanReadableName })
        Log.d("SettingsPage", "Colorize expressions: ${state.colorizeExpressions}")
        SwitchListItem(state.colorizeExpressions, {
            Log.d("SettingsPage", "Colorize expressions: $it")
            vm.setColorizeExpressions(it)
        }, "Colorize expressions", Icons.Rounded.FormatColorText)
    }
}

@Composable
fun SwitchListItem(
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
    title: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier
) {
    ListItem(modifier = modifier.clickable(onClick = {
        onCheckedChange(!checked)
    }), trailingContent = {
        Switch(checked = checked, onCheckedChange = null) // handled by parent
    }, headlineContent = {
        Text(title)
    }, leadingContent = {
        Icon(
            leadingIcon, contentDescription = ""
        )
    })
}

@Composable
fun <E : Enum<*>> EnumListItem(
    selectedOption: E,
    options: Array<E>,
    onOptionSelected: (E) -> Unit,
    title: String,
    icon: ImageVector,
    labelProvider: (E) -> String,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    ListItem(leadingContent = {
        Icon(
            icon, contentDescription = ""
        )
    }, headlineContent = {
        Text(title)
    }, supportingContent = {
        Text(labelProvider(selectedOption))
    }, modifier = modifier.clickable(onClick = {
        showDialog = true
    }))
    if (showDialog) {
        Dialog(onDismissRequest = {
            showDialog = false
        }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    Text(
                        title,
                        modifier = Modifier.padding(16.dp),
                        style = AppTypography.headlineSmall
                    )
                    Column(
                        Modifier
                            .selectableGroup()
                            .verticalScroll(rememberScrollState())
                    ) {
                        options.forEach {
                            Row(
                                Modifier
                                    .clickable {
                                        onOptionSelected(it)
                                        showDialog = false
                                    }
                                    .fillMaxWidth()
                                    .padding(16.dp, 0.dp),
                                verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = selectedOption == it, onClick = null
                                )
                                Text(
                                    labelProvider(it),
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun NumberListItem(
    value: Int,
    onValueChanged: (Int) -> Unit,
    title: String,
    icon: ImageVector,
    supportingContent: @Composable ((Int) -> Unit),
    modifier: Modifier = Modifier
) {
    var selectorValue by remember { mutableIntStateOf(value) }
    var showDialog by remember { mutableStateOf(false) }

    ListItem(leadingContent = {
        Icon(
            icon, contentDescription = ""
        )
    }, headlineContent = {
        Text(title)
    }, supportingContent = { supportingContent(value) }, modifier = modifier.clickable {
        showDialog = true
    })

    if (showDialog) {
        Dialog(onDismissRequest = {
            showDialog = false
            onValueChanged(selectorValue)
        }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        title,
                        style = AppTypography.headlineSmall
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(onClick =  {
                                selectorValue = max(-1, selectorValue - 1)
                            }, contentPadding = PaddingValues(0.dp), modifier = Modifier.size(64.dp)) {
                                Text("\u2212", style = AppTypography.displaySmall)
                            }
                            Text(
                                text = if (selectorValue <= -1) "\u221E" else selectorValue.toString(),
                                style = AppTypography.displaySmall,
                                modifier = Modifier.defaultMinSize(64.dp),
                                textAlign = TextAlign.Center
                            )
                            Button(onClick =  {
                                selectorValue = max(-1, selectorValue + 1)
                            }, contentPadding = PaddingValues(0.dp), modifier = Modifier.size(64.dp)) {
                                Text("\u002b", style = AppTypography.displaySmall)
                            }
                        }
                    }
                    TextButton(onClick = {
                        showDialog = false
                        onValueChanged(selectorValue)
                    }, modifier = Modifier.align(Alignment.End)) {
                        Text("Save")
                    }
                }
            }

        }
    }


}