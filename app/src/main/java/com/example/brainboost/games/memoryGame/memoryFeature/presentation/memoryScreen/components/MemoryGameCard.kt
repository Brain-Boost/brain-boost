package com.example.brainboost.memoryGame.memoryFeature.presentation.memoryScreen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.brainboost.memoryGame.memoryFeature.domain.model.MemoryCard
import com.example.brainboost.memoryGame.memoryFeature.presentation.memoryScreen.MemoryState
import com.example.brainboost.memoryGame.memoryFeature.presentation.util.NumericalValues.card_image_aspect_ratio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryGameCard (
    card: MemoryCard,
    modifier: Modifier = Modifier,
    state: MemoryState,
    onClick: () -> Unit
) {
    if (card.isBackDisplayed) {
        val localDensity = LocalDensity.current
        var cardHeight by remember { mutableStateOf(0.dp) }
        var cardWidth by remember { mutableStateOf(0.dp) }
        Card(
            shape = RoundedCornerShape(12.dp),
            onClick = onClick,
            colors = CardDefaults.cardColors(containerColor = state.currentTheme.cardBaseColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = modifier
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .onGloballyPositioned { coordinates ->
                        cardHeight = with(localDensity) { coordinates.size.height.toDp() }
                        cardWidth = with(localDensity) { coordinates.size.width.toDp() }
                    },
                contentAlignment = Alignment.Center
            ) {//for the back of the card
                val cardAspectRatio = cardWidth / cardHeight
                val shouldUseFillWidth = cardAspectRatio > card_image_aspect_ratio
                Image(
                    painter = painterResource(id = state.currentTheme.cardback),
                    contentDescription = "Back of card image",
                    contentScale =
                    if (shouldUseFillWidth) ContentScale.FillWidth else ContentScale.FillHeight,
                    modifier = Modifier.fillMaxSize(),
                    alignment = Alignment.Center
                )
                Text(
                    text = "?",
                    fontSize = 70.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = state.currentTheme.textColor,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black, offset = Offset(1f, 0f), blurRadius = 40f
                        )
                    )
                )
            }
        }
    } else {
        val borderModifier = if (card.matchFound) {
            modifier.border(
                width = 4.dp,
                shape = RoundedCornerShape(12.dp),
                color = state.currentTheme.matchedOutlineColor
            )
        } else {
            modifier
        }
        OutlinedCard(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = state.currentTheme.cardFrontBaseColor
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = borderModifier
        ) {
            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = state.currentTheme.getImageResourceForNumber(card.value)!!),
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = "memory card ${card.value}",
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center
                )
            }
        }
    }
}