package com.example.glowbridge.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.glowbridge.ui.screens.HomePage
import com.example.glowbridge.ui.screens.LoginScreen
import com.example.glowbridge.ui.screens.Welcome
import com.example.glowbridge.ui.screens.ProductSearchScreen
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
                    navController.navigate("search")
                }
            )
        }

        composable("HomePage"){
            HomePage(
            )
        }

        composable("search") { ProductSearchScreen() }
        composable(BottomNavItem.Home.route) { HomePage() }


    }
}



