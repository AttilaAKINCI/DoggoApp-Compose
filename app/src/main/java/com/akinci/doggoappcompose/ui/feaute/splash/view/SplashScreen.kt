package com.akinci.doggoappcompose.ui.feaute.splash

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.akinci.doggoappcompose.R
import com.akinci.doggoappcompose.ui.components.PageNavigator
import com.akinci.doggoappcompose.ui.theme.DoggoAppComposeTheme

@Composable
fun SplashScreenBody(
    onClick : ()->Unit
) {
    PageNavigator(
        R.string.splash_page,
        onClick = onClick
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DoggoAppComposeTheme {
        SplashScreenBody(onClick = { })
    }
}