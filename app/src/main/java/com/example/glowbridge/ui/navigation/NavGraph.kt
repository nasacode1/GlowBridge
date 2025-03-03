package com.example.glowbridge.ui.navigation

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.glowbridge.data.repository.AuthRepository
import com.example.glowbridge.data.repository.StreakTaskRepository
import com.example.glowbridge.ui.screens.HomePage
import com.example.glowbridge.ui.screens.LoginScreen
import com.example.glowbridge.ui.screens.MeetingPageScreen
import com.example.glowbridge.ui.screens.SearchByBarcodeScreen
import com.example.glowbridge.ui.screens.StreakPage
import com.example.glowbridge.ui.screens.Welcome
import com.example.glowbridge.ui.screens.calendlyEmbed
import com.example.glowbridge.viewmodel.ProductSearchViewModel

@Composable
fun NavGraph(navController: NavHostController, context: Context){
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
                repository = AuthRepository(),
                onLoginSuccess = { username ->
                    navController.navigate("HomePage")
                }
            )
        }

        composable("HomePage"){
            HomePage(
                onSearchByBarcodeClick = {
                    Log.e("NAVIGATION", "Navigating to SearchByBarcodeScreen")

                    navController.navigate("SearchByBarcodeScreen")
                }
            )
        }

        composable("SearchByBarcodeScreen"){ backStackEntry ->
            Log.e("COMPOSE", "SearchByBarcodeScreen is composed")

            val viewModel: ProductSearchViewModel = viewModel()

            SearchByBarcodeScreen( viewModel
            )
        }

        composable("meeting"){
            calendlyEmbed()
        }

        composable("streak"){
            val sharedPreferences = remember {
                context.getSharedPreferences("streak_prefs", Context.MODE_PRIVATE)
            }

            StreakPage(sharedPreferences)
        }


        }

    }




