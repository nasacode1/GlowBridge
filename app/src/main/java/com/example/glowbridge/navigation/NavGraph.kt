package com.example.glowbridge.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.glowbridge.ui.screens.LoginScreen

@Composable
fun NavGraph(navController: NavHostController){
    NavHost(navController = navController,
        startDestination = "LoginScreen"){
        composable("LoginScreen"){
            LoginScreen()
        }

    }
}