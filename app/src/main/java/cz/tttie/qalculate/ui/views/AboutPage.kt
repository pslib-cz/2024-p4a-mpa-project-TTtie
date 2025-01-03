package cz.tttie.qalculate.ui.views

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cz.tttie.qalculate.ui.theme.AppTypography

@Composable
fun AboutPage(modifier: Modifier = Modifier) {
    val ctx = LocalContext.current
    Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            "Calculator",
            style = AppTypography.headlineLarge
        )
        Text("built using libqalculate v5.4.0 by TTtie")
        Button(onClick = {
            ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Qalculate/libqalculate")))
        }, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "libqalculate on GitHub"
            )
        }
    }
}