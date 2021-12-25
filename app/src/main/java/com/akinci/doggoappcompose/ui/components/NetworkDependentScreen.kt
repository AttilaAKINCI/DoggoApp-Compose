package com.akinci.doggoappcompose.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.akinci.doggoappcompose.common.network.NetworkState
import com.akinci.doggoappcompose.common.network.NetworkStateViewModel

@Composable
fun NetworkDependentScreen(
    modifier: Modifier = Modifier,
    retryAction: () -> Unit,
    networkStateViewModel: NetworkStateViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    val networkState by networkStateViewModel.networkState.collectAsState(initial = NetworkState.None)

    Box(modifier = modifier){
        OfflineDialog(networkState = networkState) { retryAction() }
        content()
    }
}