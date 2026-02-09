package com.example.transactiontracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.transactiontracker.ui.screens.homescreen.HomeScreen
import com.example.transactiontracker.ui.screens.transaction.TransactionScreen

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
){
    NavHost (
        navController = navController,
        startDestination = NavRoutes.HOME
    )
    {
        composable(NavRoutes.HOME) {
            HomeScreen(
                onNavigateToTransaction = {
                    navController.navigate(NavRoutes.TRANSACTION)
                }
            )
        }
        composable(NavRoutes.TRANSACTION) {
            TransactionScreen()
        }
        composable(NavRoutes.STATS) {

        }

    }
}