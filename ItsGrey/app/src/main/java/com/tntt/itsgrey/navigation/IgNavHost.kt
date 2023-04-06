package com.tntt.itsgrey.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.tntt.editbook.Navigation.editBookPageRoute
import com.tntt.editbook.Navigation.editBookPageScreen
import com.tntt.feature.editpage.navigation.editPageScreen
import com.tntt.home.navigation.homePageScreen
import com.tntt.viewer.Navigation.viewerPageRoute
import com.tntt.viewer.Navigation.viewerPageScreen
import itsgrey.feature.drawing.navigation.*

@Composable
fun IgNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    currentUserEmail: String,
    currentUserName:String,
//    startDestination: String = editBookPageRoute
    startDestination: String = "$homePageRoute/{userId}/{userName}"
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,

    ) {
        editPageScreen(
            onBackClick = {},
            onImageClick = { id, uri ->
                navController.toDrawing(id, uri)
            },
            navController = navController
        )
        drawingScreen(
            onBackClick = {
                navController.navigate(editPageGraphRoutePattern) {
                    popUpTo(editPageGraphRoutePattern) {
                        inclusive = true
                    }
                }
            }
        )
        homePageScreen(
            onThumbnailClick = {
//                navController.navigateToBook(stirng)
                               },
            currentUserEmail = currentUserEmail,
            currentUserName = currentUserName
        )
        editBookPageScreen(
            onBackClick = {},
            onViewerClick = {},
            onNewPageClick = {},
//            onViewerClick = { id -> navController.toViewer(id) }
            currentUserEmail = currentUserEmail,
        )
        viewerPageScreen(
            onBackClick = {},
        )
    }
}