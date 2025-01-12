package cz.tttie.qalculate.ui.views

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Backspace
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Keyboard
import androidx.compose.material.icons.rounded.KeyboardHide
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.InterceptPlatformTextInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cz.tttie.qalculate.ui.utils.LongPressTonalButton
import cz.tttie.qalculate.ui.utils.LongPressTonalIconButton
import cz.tttie.qalculate.ui.vm.CalculatorViewModel
import kotlinx.coroutines.awaitCancellation

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainPage(rootVm: CalculatorViewModel, modifier: Modifier = Modifier) {
    val comma = rootVm.useQalc { it.comma }
    val commaChar = comma.toCharArray()[0]
    val state by rootVm.state.collectAsState()

    val focusRequester = remember { FocusRequester() }
    val clipboard = LocalClipboardManager.current
    val ctx = LocalContext.current
    val keyb = LocalSoftwareKeyboardController.current

    Column(modifier) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (!state.keyboardMode) {
                InterceptPlatformTextInput({ _, _ -> awaitCancellation() }) {
                    TextField(
                        value = state.expression,
                        rootVm::onTextFieldValueUpdate,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            showKeyboardOnFocus = false
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                    )
                }
            } else {
                TextField(
                    value = state.expression,
                    rootVm::onTextFieldValueUpdate,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        showKeyboardOnFocus = false
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                )
            }

            AnimatedVisibility(
                state.result != null,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                val result = AnnotatedString.fromHtml(state.result?.htmlResult ?: "")

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append(
                                when (state.result?.isApproximate) {
                                    true -> "${Typography.almostEqual} "
                                    false -> "= "
                                    else -> ""
                                }
                            )
                            append(AnnotatedString.fromHtml(state.result?.htmlResult ?: ""))
                        },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(8.dp)
                    )

                    if (state.result?.htmlResult != null) {
                        FilledTonalIconButton({
                            clipboard.setText(result)
                            Toast.makeText(ctx, "Result copied to clipboard", Toast.LENGTH_SHORT)
                                .show()
                        }) {
                            Icon(
                                Icons.Rounded.ContentCopy, contentDescription = "Copy"
                            )
                        }
                    }
                }
            }
        }

        AnimatedVisibility(state.keyboardMode, modifier = Modifier.padding(8.dp, 0.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton({
                    rootVm.toggleKeyboardMode()
                }, modifier = Modifier.weight(1f)) {
                    Icon(
                        Icons.Rounded.KeyboardHide, contentDescription = "Disable advanced mode"
                    )
                }

                Button({
                    rootVm.calculate()
                }, modifier = Modifier.weight(1f)) {
                    Text("=")
                }
            }


        }
        val buttonModifier = Modifier
            .weight(1f)
            .fillMaxHeight()
        val spacing = Arrangement.spacedBy(8.dp)
        AnimatedVisibility(
            !state.keyboardMode, modifier = Modifier.heightIn(Dp.Unspecified, 380.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp), verticalArrangement = spacing
            ) {
                val rowModifier = Modifier.weight(1f)

                Row(modifier = rowModifier, horizontalArrangement = spacing) {
                    LongPressTonalIconButton(
                        { rootVm.backspace() },
                        { rootVm.clearExpression() },
                        modifier = buttonModifier
                    ) {
                        Icon(
                            Icons.AutoMirrored.Rounded.Backspace, contentDescription = "Backspace"
                        )
                    }
                    FilledTonalIconButton({
                        rootVm.toggleKeyboardMode()
                    }, modifier = buttonModifier) {
                        Icon(
                            Icons.Rounded.Keyboard, contentDescription = "Advanced mode"
                        )
                    }
                    for (x in arrayOf('(' to '[', ')' to ']', '%' to null)) {
                        key(x) {
                            AppendingTonalButton(
                                rootVm,
                                x.first.toString(),
                                x.first.toString(),
                                x.second.toString(),
                                buttonModifier
                            )
                        }
                    }
                }

                for (texts in listOf(
                    listOf(
                        "7" to null,
                        "8" to null,
                        "9" to null,
                        Typography.times.toString() to null,
                        "\u00f7" to null
                    ),
                    listOf(
                        "4" to null, "5" to null, "6" to null, "\u2212" to null, "\u002b" to null
                    ),
                    listOf("1" to null,
                        "2" to null,
                        "3" to null,
                        comma to null,
                        "^" to buildAnnotatedString {
                            append("x")
                            withStyle(SpanStyle(baselineShift = BaselineShift.Superscript)) {
                                append("y")
                            }
                        }),
                )) {
                    key(texts) {
                        Row(modifier = rowModifier, horizontalArrangement = spacing) {
                            for (x in texts) {
                                key(x) {
                                    AppendingTonalButton(
                                        rootVm,
                                        x.second ?: AnnotatedString(x.first),
                                        x.first,
                                        modifier = buttonModifier
                                    )
                                }
                            }
                        }
                    }
                }

                Row(modifier = rowModifier, horizontalArrangement = spacing) {
                    for (x in listOf(
                        "0" to null,
                        "." to null,
                        "e" to buildAnnotatedString {
                            append("10")
                            withStyle(SpanStyle(baselineShift = BaselineShift.Superscript)) {
                                append("x")
                            }
                        },
                        "ans" to AnnotatedString("Ans"),
                    )) {
                        key(x) {
                            AppendingTonalButton(
                                rootVm,
                                x.second ?: AnnotatedString(x.first),
                                x.first,
                                modifier = buttonModifier
                            )
                        }
                    }

                    Button({
                        rootVm.calculate()
                    }, modifier = buttonModifier, contentPadding = PaddingValues(0.dp)) {
                        Text("=")
                    }
                }
            }
        }
    }

    LaunchedEffect(state.keyboardMode) {
        if (state.keyboardMode) {
            keyb?.show()
        } else {
            keyb?.hide()
        }
        focusRequester.requestFocus()
    }
}

@Composable
fun AppendingTonalButton(
    rootVm: CalculatorViewModel,
    text: String,
    toAppend: String,
    toAppendLong: String? = null,
    modifier: Modifier = Modifier
) {
    AppendingTonalButton(rootVm, AnnotatedString(text), toAppend, toAppendLong, modifier)
}

@Composable
fun AppendingTonalButton(
    rootVm: CalculatorViewModel,
    text: AnnotatedString,
    toAppend: String,
    toAppendLong: String? = null,
    modifier: Modifier = Modifier
) {
    LongPressTonalButton(onClick = {
        rootVm.appendToExpression(toAppend)
    }, onLongClick = {
        rootVm.appendToExpression(toAppendLong ?: toAppend)
    }, contentPadding = PaddingValues(0.dp), modifier = modifier
    ) {
        Text(text)
    }
}