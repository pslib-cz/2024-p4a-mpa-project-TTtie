package cz.tttie.qalculate.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Functions
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded._123
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cz.tttie.qalculate.binding.Qalculate
import cz.tttie.qalculate.ui.views.AboutPage
import cz.tttie.qalculate.ui.views.FunctionsPage
import cz.tttie.qalculate.ui.views.HistoryPage
import cz.tttie.qalculate.ui.views.MainPage
import cz.tttie.qalculate.ui.views.SettingsPage
import cz.tttie.qalculate.ui.views.VariablesPage
import cz.tttie.qalculate.ui.vm.CalculatorViewModel
import cz.tttie.qalculate.ui.vm.SettingsPageViewModel

val LocalCalculator =
    staticCompositionLocalOf<CalculatorViewModel> { throw IllegalStateException("Calculator not provided!") }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(qalc: Qalculate) {
    val nav = rememberNavController()
    val ctx = LocalContext.current

    val vm = viewModel<CalculatorViewModel>(factory = viewModelFactory {
        addInitializer(CalculatorViewModel::class) {
            CalculatorViewModel(ctx, qalc)
        }
    })

    CompositionLocalProvider(LocalCalculator provides vm) {
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar(navigationIcon = {
                val currentBackStackEntry by nav.currentBackStackEntryAsState()
                if (currentBackStackEntry != null && nav.previousBackStackEntry != null) {
                    IconButton(onClick = {
                        nav.popBackStack()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Go back"
                        )
                    }
                }
            }, title = {}, actions = {
                IconButton(onClick = {
                    if (nav.currentDestination?.route != "/history") nav.navigate("/history") {
                        popUpTo("/") {}
                    }
                }) {
                    Icon(
                        Icons.Rounded.History, contentDescription = "History"
                    )
                }
                IconButton(onClick = {
                    if (nav.currentDestination?.route != "/variables") nav.navigate("/variables") {
                        popUpTo("/") {}
                    }
                }) {
                    Icon(
                        Icons.Rounded._123, contentDescription = "Variables"
                    )
                }
                IconButton(onClick = {
                    if (nav.currentDestination?.route != "/functions") nav.navigate("/functions") {
                        popUpTo("/") {}
                    }
                }) {
                    Icon(
                        Icons.Rounded.Functions, contentDescription = "Functions"
                    )
                }
                IconButton(onClick = {
                    if (nav.currentDestination?.route != "/settings") nav.navigate("/settings") {
                        popUpTo("/") {}
                    }
                }) {
                    Icon(
                        Icons.Rounded.Settings, contentDescription = "Settings"
                    )
                }
                IconButton(onClick = {
                    if (nav.currentDestination?.route != "/about") nav.navigate("/about") {
                        popUpTo("/") {}
                    }
                }) {
                    Icon(
                        Icons.Rounded.Info, contentDescription = "About"
                    )
                }
            })
        }, contentWindowInsets = WindowInsets.safeDrawing) { innerPadding ->
            NavHost(navController = nav, startDestination = "/") {
                composable("/") {
                    MainPage(
                        rootVm = vm, modifier = Modifier.padding(innerPadding)
                    )
                }
                composable("/about") {
                    AboutPage(modifier = Modifier.padding(innerPadding))
                }
                composable("/functions") {
                    FunctionsPage(nav, modifier = Modifier.padding(innerPadding))
                }
                composable("/variables") {
                    VariablesPage(nav, modifier = Modifier.padding(innerPadding))
                }
                composable("/settings") {
                    val settingsVm = viewModel<SettingsPageViewModel>(factory = viewModelFactory {
                        addInitializer(SettingsPageViewModel::class) {
                            SettingsPageViewModel(vm, ctx)
                        }
                    })
                    SettingsPage(
                        vm = settingsVm, modifier = Modifier.padding(innerPadding)
                    )
                }
                composable("/history") {
                    HistoryPage(nav, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}