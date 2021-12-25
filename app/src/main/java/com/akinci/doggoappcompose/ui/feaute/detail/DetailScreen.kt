package com.akinci.doggoappcompose.ui.feaute.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.akinci.doggoappcompose.R
import com.akinci.doggoappcompose.ui.components.PageNavigator
import com.akinci.doggoappcompose.ui.theme.DoggoAppComposeTheme

@Composable
fun DetailScreenBody(
    onClick : ()->Unit
) {
    PageNavigator(
        navigatorMessageId = R.string.detail_page,
        navigateButtonMessageId = R.string.button_prev_page,
        onClick = onClick
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DoggoAppComposeTheme {
        DetailScreenBody(onClick = { })
    }
}