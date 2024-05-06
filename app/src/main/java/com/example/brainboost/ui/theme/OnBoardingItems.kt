package com.example.brainboost.ui.theme

import com.example.brainboost.R

class OnBoardingItems(
    val image: Int,
    val title: Int,
    val desc: Int
) {
    companion object{
        fun getData(): List<OnBoardingItems>{
            return listOf(
                OnBoardingItems(R.drawable.brain_boost_for_card, R.string.onBoardingTitle1, R.string.onBoardingText1),
                OnBoardingItems(R.drawable.main_screen_1,R.string.onBoardingTitle2, R.string.onBoardingText2),
                OnBoardingItems(R.drawable.main_screen_2, R.string.onBoardingTitle3, R.string.onBoardingText3),
                OnBoardingItems(R.drawable.main_screen_3, R.string.onBoardingTitle4, R.string.onBoardingText4),
                OnBoardingItems(R.drawable.brain_boost_for_card, R.string.onBoardingTitle5, R.string.onBoardingText5)
            )
        }
    }
}