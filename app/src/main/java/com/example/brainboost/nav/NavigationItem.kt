package com.example.brainboost.nav

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val badgeCount: Int? = null
)
