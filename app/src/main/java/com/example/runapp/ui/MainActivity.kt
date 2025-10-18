package com.example.runapp.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.runapp.ui.screens.current_run.CurrentRunScreen
import com.example.runapp.ui.screens.run.RunScreen
import com.example.runapp.ui.screens.stat.StaticScreen
import com.example.runapp.ui.theme.RunAppTheme
import com.example.runapp.utils.MyBottomNavigation
import com.example.runapp.utils.Screens
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {


            RunAppTheme {
                val navController = rememberNavController()
                val navBackStackEntry = navController.currentBackStackEntryAsState()
                val currentScreen =
                    Screens.valueOf(navBackStackEntry.value?.destination?.route ?: Screens.Run.name)
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = stringResource(currentScreen.title)
                                )
                            },
                            navigationIcon = {
                                if (currentScreen == Screens.CurrentRun) {
                                    IconButton(
                                        onClick = {
                                            navController.navigate(Screens.Run.name) {
                                                popUpTo(0) {
                                                    inclusive = true
                                                } // полностью очистить весь стек
                                                launchSingleTop = true
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowBack,
                                            contentDescription = null
                                        )
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )

                    },
                    modifier = Modifier.Companion.fillMaxSize(),
                    bottomBar = {
                        if (currentScreen != Screens.CurrentRun) {
                            MyBottomNavigation(navController = navController)
                        }


                    }
                ) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = Screens.Run.name
                    ) {

                        composable(Screens.Run.name) {
                            RunScreen(
                                navController = navController,
                                innerPaddingValues = innerPadding
                            )
                        }
                        composable(Screens.Statistic.name) {
                            StaticScreen()
                        }
                        composable(Screens.CurrentRun.name) {
                            CurrentRunScreen(
                                navController = navController,
                                innerPaddingValues = innerPadding
                            )
                        }

                    }


                }
            }
        }
    }
}


