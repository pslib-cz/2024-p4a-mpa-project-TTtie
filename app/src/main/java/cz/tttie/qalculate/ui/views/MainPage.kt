@file:OptIn(ExperimentalLayoutApi::class)

package cz.tttie.qalculate.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Backspace
import androidx.compose.material.icons.rounded.Keyboard
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import cz.tttie.qalculate.ui.vm.CalculatorViewModel

@Composable
fun MainPage(rootVm: CalculatorViewModel, modifier: Modifier = Modifier) {
    val comma = rootVm.useQalc { it.comma }
    val state by rootVm.state.collectAsState()

    Column(modifier) {
        Column(modifier = Modifier.weight(4f)) {
            TextField(
                value = state.expression,
                {},
                readOnly = true,
                modifier = Modifier.fillMaxSize()
            )
        }

        val buttonModifier = Modifier
            .weight(1f)
            .fillMaxHeight()
        val spacing = Arrangement.spacedBy(8.dp)
        Column(modifier = Modifier
            .weight(6f)
            .padding(8.dp), verticalArrangement = spacing) {
            val rowModifier = Modifier.weight(1f)

            Row(modifier = rowModifier, horizontalArrangement = spacing) {
                FilledTonalIconButton({}, modifier = buttonModifier) {
                    Icon(
                        Icons.AutoMirrored.Rounded.Backspace, contentDescription = "Backspace"
                    )
                }
                FilledTonalIconButton({}, modifier = buttonModifier) {
                    Icon(
                        Icons.Rounded.Keyboard, contentDescription = "Advanced mode"
                    )
                }
                FilledTonalButton(
                    {},
                    modifier = buttonModifier,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("(")
                }
                FilledTonalButton(
                    {},
                    modifier = buttonModifier,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(")")
                }
                FilledTonalButton(
                    {},
                    modifier = buttonModifier,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("%")
                }
            }
            Row(modifier = rowModifier, horizontalArrangement = spacing) {
                FilledTonalButton(
                    {},
                    modifier = buttonModifier,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("7")
                }
                FilledTonalButton(
                    {},
                    modifier = buttonModifier,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("8")
                }
                FilledTonalButton(
                    {},
                    modifier = buttonModifier,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("9")
                }
                FilledTonalButton(
                    {},
                    modifier = buttonModifier,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("${Typography.times}")
                }
                FilledTonalButton(
                    {},
                    modifier = buttonModifier,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("\u00f7")
                }
            }

            Row(modifier = rowModifier, horizontalArrangement = spacing) {
                FilledTonalButton(
                    {},
                    modifier = buttonModifier,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("4")
                }
                FilledTonalButton(
                    {},
                    modifier = buttonModifier,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("5")
                }
                FilledTonalButton(
                    {},
                    modifier = buttonModifier,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("6")
                }
                FilledTonalButton(
                    {},
                    modifier = buttonModifier,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("\u2212")
                }
                FilledTonalButton(
                    {},
                    modifier = buttonModifier,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("\u002b")
                }
            }
            Row(modifier = rowModifier, horizontalArrangement = spacing) {
                FilledTonalButton(
                    {},
                    modifier = buttonModifier,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("1")
                }
                FilledTonalButton(
                    {},
                    modifier = buttonModifier,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("2")
                }
                FilledTonalButton(
                    {},
                    modifier = buttonModifier,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("3")
                }
                FilledTonalButton(
                    {},
                    modifier = buttonModifier,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(comma)
                }
                FilledTonalButton(
                    {},
                    modifier = buttonModifier,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(buildAnnotatedString {
                        append("x")
                        withStyle(SpanStyle(baselineShift = BaselineShift.Superscript)) {
                            append("y")
                        }
                    })
                }
            }
            Row(modifier = rowModifier, horizontalArrangement = spacing) {
                FilledTonalButton(
                    {},
                    modifier = buttonModifier,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("0")
                }
                FilledTonalButton(
                    {},
                    modifier = buttonModifier,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(".")
                }
                FilledTonalButton(
                    {},
                    modifier = buttonModifier,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(buildAnnotatedString {
                        append("10")
                        withStyle(SpanStyle(baselineShift = BaselineShift.Superscript)) {
                            append("x")
                        }
                    })
                }
                FilledTonalButton(
                    {},
                    modifier = buttonModifier,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Ans")
                }
                Button({}, modifier = buttonModifier, contentPadding = PaddingValues(0.dp)) {
                    Text("=")
                }
            }
        }
    }
}