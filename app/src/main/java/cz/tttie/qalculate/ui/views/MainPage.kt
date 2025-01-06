package cz.tttie.qalculate.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.InterceptPlatformTextInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import cz.tttie.qalculate.ui.vm.CalculatorViewModel
import kotlinx.coroutines.awaitCancellation

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainPage(rootVm: CalculatorViewModel, modifier: Modifier = Modifier) {
    val comma = rootVm.useQalc { it.comma }
    val state by rootVm.state.collectAsState()

    val focusRequester = remember { FocusRequester() }

    Column(modifier) {
        Column(modifier = Modifier.weight(4f)) {
            InterceptPlatformTextInput({rq, nx -> awaitCancellation()}) {
                TextField(
                    value = state.expression,
                    rootVm::onTextFieldValueUpdate,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        showKeyboardOnFocus = false
                    ),
                    modifier = Modifier.fillMaxSize().focusRequester(focusRequester)
                )
            }
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
                FilledTonalIconButton({ rootVm.backspace() }, modifier = buttonModifier) {
                    Icon(
                        Icons.AutoMirrored.Rounded.Backspace, contentDescription = "Backspace"
                    )
                }
                FilledTonalIconButton({}, modifier = buttonModifier) {
                    Icon(
                        Icons.Rounded.Keyboard, contentDescription = "Advanced mode"
                    )
                }
                for (x in arrayOf('(' to '[', ')' to ']', '%' to null)) {
                    key(x) {
                        AppendingTonalButton(rootVm, x.first.toString(), x.first, x.second, buttonModifier)
                    }
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
                Button({
                    rootVm.calculate()
                }, modifier = buttonModifier, contentPadding = PaddingValues(0.dp)) {
                    Text("=")
                }
            }
        }
    }

    LaunchedEffect(true) {
        focusRequester.requestFocus()
    }
}

@Composable
fun AppendingTonalButton(
    rootVm: CalculatorViewModel,
    text: String,
    toAppend: Char,
    toAppendLong: Char? = null,
    modifier: Modifier = Modifier) {
    AppendingTonalButton(rootVm, AnnotatedString(text), toAppend, toAppendLong, modifier)
}

@Composable
fun AppendingTonalButton(
    rootVm: CalculatorViewModel,
    text: AnnotatedString,
    toAppend: Char,
    toAppendLong: Char? = null,
    modifier: Modifier = Modifier) {
    FilledTonalButton(onClick = {
        rootVm.appendToExpression(toAppend)
    }, contentPadding = PaddingValues(0.dp), modifier = modifier) {
        Text(text)
    }

}