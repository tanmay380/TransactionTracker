package com.example.transactiontracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.transactiontracker.ui.screens.cardhistory.CardHistoryScreen
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
                onNavigateToTransaction = { cardNo ->
                    navController.navigate("${NavRoutes.CARD_HISTORY}/$cardNo")
                }
            )
        }
        composable(NavRoutes.TRANSACTION) {
            TransactionScreen()
        }
        composable(NavRoutes.STATS) {

        }
        composable("${NavRoutes.CARD_HISTORY}/{cardNumber}",
            arguments = listOf(
                navArgument("cardNumber"){
                    type = NavType.StringType
                }
            )) {
            val cardNo = it.arguments?.getString("cardNumber")!!
            CardHistoryScreen(cardNo)
        }


    }
}