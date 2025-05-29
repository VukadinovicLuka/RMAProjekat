package com.example.prviprojekatrma.drawer

sealed class AppDrawerDestination {
    object Profile : AppDrawerDestination()
    object Breeds : AppDrawerDestination()
    object Quiz : AppDrawerDestination()
    object Leaderboard: AppDrawerDestination()
}