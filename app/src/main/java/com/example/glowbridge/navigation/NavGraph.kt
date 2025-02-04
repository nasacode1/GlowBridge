package com.example.glowbridge.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.glowbridge.ui.screens.HomePage
import com.example.glowbridge.ui.screens.LoginScreen
import com.example.glowbridge.ui.screens.Welcome

@Composable
fun NavGraph(navController: NavHostController){
    NavHost(navController = navController,
        startDestination = "Welcome"){

        composable("Welcome"){
            Welcome(
                onStartedSuccess = {
                    navController.navigate("LoginScreen")
                }
            )
        }

        composable("LoginScreen"){
            LoginScreen(
                onLoginSuccess = { username ->
                    navController.navigate("HomePage")
                }
            )
        }

        composable("HomePage"){
            HomePage(
            )
        }
        composable(BottomNavItem.Home.route) { HomePage() }
    }
}



