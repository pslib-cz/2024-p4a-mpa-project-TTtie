package cz.tttie.qalculate

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cz.tttie.qalculate.binding.Qalculate
import cz.tttie.qalculate.ui.AppScaffold
import cz.tttie.qalculate.ui.theme.QalculateTheme

class MainActivity : ComponentActivity() {
    lateinit var qalculate: Qalculate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.d("MainActivity", "onCreate")
        qalculate = Qalculate(this)
        setContent {
            QalculateTheme {
                AppScaffold(qalculate)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        qalculate.close()
    }
}