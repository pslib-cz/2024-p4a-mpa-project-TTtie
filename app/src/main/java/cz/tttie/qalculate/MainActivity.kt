package cz.tttie.qalculate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cz.tttie.qalculate.ui.AppScaffold
import cz.tttie.qalculate.ui.theme.QalculateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QalculateTheme {
                AppScaffold()
            }
        }
    }
}