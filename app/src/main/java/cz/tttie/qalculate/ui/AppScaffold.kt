package cz.tttie.qalculate.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Functions
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cz.tttie.qalculate.binding.Qalculate
import cz.tttie.qalculate.ui.views.AboutPage
import cz.tttie.qalculate.ui.views.FunctionsPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold() {
    val nav = rememberNavController()
    val ctx = LocalContext.current
    val qalc = remember { Qalculate(ctx) }
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    val currentBackStackEntry by nav.currentBackStackEntryAsState()
                    if (currentBackStackEntry != null && nav.previousBackStackEntry != null) {
                        Log.d("AppScaffold", "recompose!")
                        IconButton(onClick = {
                            nav.popBackStack()
                        }) {
                            Icon(
                                Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "Go back"
                            )
                        }
                    }
                },
                title = {},
                actions = {
                    IconButton(onClick = {
                        if (nav.currentDestination?.route != "/functions")
                            nav.navigate("/functions") {
                                popUpTo("/") {}
                            }
                    }) {
                        Icon(
                            Icons.Rounded.Functions,
                            contentDescription = "Functions"
                        )
                    }
                    IconButton(onClick = {
                        if (nav.currentDestination?.route != "/about")
                            nav.navigate("/about") {
                                popUpTo("/") {}
                            }
                    }) {
                        Icon(
                            Icons.Rounded.Info,
                            contentDescription = "About"
                        )
                    }
                }
            )
        }) { innerPadding ->
        NavHost(navController = nav, startDestination = "/") {
            composable("/") {
                Column(modifier = Modifier.padding(innerPadding)) {
                    Text("hi!")
                }
            }
            composable("/about") {
                AboutPage(modifier = Modifier.padding(innerPadding))
            }
            composable("/functions") {
                FunctionsPage(qalc, modifier = Modifier.padding(innerPadding))
            }
        }
    }
}