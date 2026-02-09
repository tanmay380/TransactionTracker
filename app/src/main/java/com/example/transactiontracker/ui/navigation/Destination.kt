package com.example.transactiontracker.ui.navigation

sealed class Destination(val route: String) {
    object Home : Destination(NavRoutes.HOME)
    object Transaction : Destination(NavRoutes.TRANSACTION)
    object Stats : Destination(NavRoutes.STATS)
}