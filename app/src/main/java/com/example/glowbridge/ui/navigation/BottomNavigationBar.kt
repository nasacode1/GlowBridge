package com.example.glowbridge.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import com.example.glowbridge.ui.navigation.BottomNavItem
@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Meeting,
        BottomNavItem.Profile,
        BottomNavItem.Streak)

        NavigationBar {
            val currentRoute = navController.currentDestination?.route
        items.forEach { item ->
            NavigationBarItem(

                icon = { Icon(item.icon, contentDescription = item.label)  },
                label = { Text(item.label) },
                selected = currentRoute == item.route, // Handle selection state dynamically
                onClick = { navController.navigate(item.route)},
            )
        }
    }
}