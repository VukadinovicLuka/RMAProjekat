package com.example.prviprojekatrma.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prviprojekatrma.breedDetails.breedDetails
import com.example.prviprojekatrma.breeds.list.breeds
import com.example.prviprojekatrma.gallery.gallery
import com.example.prviprojekatrma.gallery.photoViewer
import com.example.prviprojekatrma.leaderboard.LeaderboardScreen
import com.example.prviprojekatrma.login.LoginScreen
import com.example.prviprojekatrma.profile.AllScoresScreen
import com.example.prviprojekatrma.profile.EditProfileScreen
import com.example.prviprojekatrma.profile.ProfileScreen
import quiz


@RequiresApi(Build.VERSION_CODES.O) //zbog localtime.now u QuizViewModel
@Composable
fun BreedsListNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {
            LoginScreen(navController = navController)
        }

        breeds(
            route = "breeds",
            onUserClick = {
                navController.navigate(route = "breeds/$it")
            },
            onProfileClick = {
                navController.navigate(route = "profile")
            },
            onBreedsClick = {
                navController.navigate(route = "breeds")
            },
            onQuizClick = {
                navController.navigate(route = "quiz")
            },
            onLeaderboardClick = {
                navController.navigate(route = "leaderboard")
            }
        )

        breedDetails(
            route = "breeds/{breeds_id}",
            navController = navController
        )

        gallery(
            route = "breeds/gallery/{breeds_id}",
            navController = navController
        )

        photoViewer(
            route = "breeds/photo_viewer/{breeds_id}/{image_index}",
            navController = navController
        )

        composable("profile") {
            ProfileScreen(navController = navController)
        }

        composable("allScores") {
            AllScoresScreen(navController = navController)
        }

        composable("editProfile"){
            EditProfileScreen(navController = navController)
        }

        quiz(
            route = "quiz",
            navController = navController,
            onQuizFinish = { navController.navigate(route = "breeds") }
        )


        composable("leaderboard"){
            LeaderboardScreen(navController = navController)
        }
    }
}

inline val SavedStateHandle.breeds_id: String
    get() = checkNotNull(get("breeds_id")) { "breeds_id is mandatory" }
