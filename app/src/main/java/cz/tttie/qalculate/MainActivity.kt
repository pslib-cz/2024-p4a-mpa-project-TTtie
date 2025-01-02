package cz.tttie.qalculate

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.tooling.preview.Preview
import cz.tttie.qalculate.binding.Qalculate
import cz.tttie.qalculate.ui.theme.QalculateTheme
//import cz.tttie.sample_binding.NativeLib

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QalculateTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val test = Qalculate(LocalContext.current)
    val calc = test.calculate("solve((3 / (x + 2)) + (5x / (4−x^2)) = (3 / (x−2)) + (x / (x^2 − 4)))")
    Log.d("Calc", "printing messages:")
    for (msg in calc.messages) {
        Log.d("Calc", "${msg.type}: ${msg.message}")
    }
    Log.d("Calc", "message print ended!")
    Text(
        text = AnnotatedString.fromHtml(
            calc.htmlResult,
        ),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QalculateTheme {
        Greeting("Android")
    }
}