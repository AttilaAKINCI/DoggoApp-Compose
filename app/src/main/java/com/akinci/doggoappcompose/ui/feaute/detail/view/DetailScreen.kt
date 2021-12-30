package com.akinci.doggoappcompose.ui.feaute.detail.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.akinci.doggoappcompose.R
import com.akinci.doggoappcompose.common.helper.allCaps
import com.akinci.doggoappcompose.ui.components.DoggoAppBar
import com.akinci.doggoappcompose.ui.components.list.header.ListHeader
import com.akinci.doggoappcompose.ui.feaute.detail.data.Content
import com.akinci.doggoappcompose.ui.feaute.detail.viewmodel.DetailViewModel
import com.akinci.doggoappcompose.ui.theme.DoggoAppComposeTheme

/**
 * Stateful version of the Podcast player
 */
@Composable
fun DetailScreen(
    args: DetailScreenArgs,
    viewModel: DetailViewModel = hiltViewModel(),
    onBackPress: () -> Unit
){
    LaunchedEffect(true) {
        viewModel.getDoggoContent(args.breedName, args.subBreedName)
    }

    DetailScreenBody(
        args = args,
        vm = viewModel,
        onBackPress = onBackPress
    )
}

/**
 * Stateless version of the Player screen
 */
@Composable
fun DetailScreenBody(
    args: DetailScreenArgs,
    vm: DetailViewModel,
    onBackPress : ()->Unit
) {
    Scaffold(
        topBar = {
            DoggoAppBar(
                title = stringResource(R.string.title_detail),
                isBackEnabled = true,
                onBackPress = onBackPress
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            /** Header of doggo image list **/
            ListHeader(
                headerTitle = if(args.subBreedName.isNotBlank()) {
                    stringResource(R.string.detail_title_long, args.breedName, args.subBreedName).allCaps()
                }else{
                    stringResource(R.string.detail_title_short, args.breedName).allCaps()
                }
            )

            val contentList = remember { mutableStateListOf<Content>() }
            contentList.addAll(vm.breedImageListState)

            /** Doggo Images **/
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp)
            ){
                items(contentList) { contentItem ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(18.dp))
                    ) {
                        Image(
                            painter = rememberImagePainter(
                                data = contentItem.imageUrl,
                                builder = {
                                    crossfade(true)
                                }
                            ),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .background(colorResource(R.color.white_40))
                        ) {
                            Text(
                                text = contentItem.dogName,
                                color = colorResource(R.color.card_border),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(0.dp, 5.dp, 0.dp, 10.dp),
                                style = MaterialTheme.typography.h2,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DoggoAppComposeTheme {
        DetailScreen(
            args = DetailScreenArgs("Boxer", ""),
            onBackPress = { }
        )
    }
}