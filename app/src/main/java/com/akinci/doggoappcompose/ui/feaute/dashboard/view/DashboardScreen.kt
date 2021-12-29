package com.akinci.doggoappcompose.ui.feaute.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.akinci.doggoappcompose.R
import com.akinci.doggoappcompose.ui.components.PageNavigator
import com.akinci.doggoappcompose.ui.theme.DoggoAppComposeTheme

@Composable
fun DashboardScreenBody(
    onClick : ()->Unit
) {
    PageNavigator(
        R.string.dashboard_page,
        onClick = onClick
    )
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DoggoAppComposeTheme {
        DashboardScreenBody(onClick = { })
    }
}