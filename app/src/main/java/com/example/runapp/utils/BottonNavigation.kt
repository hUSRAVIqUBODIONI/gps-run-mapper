package com.example.runapp.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.runapp.ui.screens.run.RunScreen

@Composable
fun MyBottomNavigation(
    navController: NavHostController
){

    var curScreen by remember { mutableStateOf(0) }
    val items = listOf(
        NavItem(Screens.Run.name, Icons.Default.Lock, "Run"),
        NavItem(Screens.Statistic.name, Icons.Default.Info, "Static")
    )

    NavigationBar {
        items.forEachIndexed { ind,navItem ->
            NavigationBarItem(
                selected = ind == curScreen,
                onClick = {
                    curScreen = ind
                    navController.navigate(navItem.route)
                },
                icon = {
                    Icon(imageVector = navItem.icon, contentDescription = navItem.label)
                }
            )
        }
    }



}



data class NavItem(
    val route: String,
    val icon: ImageVector,
    val label: String

)