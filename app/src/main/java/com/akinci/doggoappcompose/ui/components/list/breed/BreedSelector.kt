package com.akinci.doggoappcompose.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akinci.doggoappcompose.R
import com.akinci.doggoappcompose.ui.components.list.StaggeredHorizontalGrid
import com.akinci.doggoappcompose.ui.components.list.breed.BreedItem
import com.akinci.doggoappcompose.ui.feaute.dashboard.data.Breed
import kotlinx.coroutines.launch


@ExperimentalAnimationApi
@Composable
fun BreedSelector(
    content: List<Breed>,
    headerTitle: String,
    isVisible: Boolean
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(
            initialAlpha = 0f
        ),
        exit = fadeOut(
            targetAlpha = 1f
        )
    ) {
        Column(
            modifier = Modifier
                .height(200.dp)
        ){
            /** Breed Header **/
            Column(
                modifier = Modifier
                    .padding(20.dp, 10.dp, 20.dp, 10.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(color = colorResource(R.color.teal_200_90)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = headerTitle,
                    modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 10.dp),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
            }

            val myList = remember { mutableStateListOf<Breed>() }
            myList.addAll(content)

            /** Breed List **/
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(myList) { item ->
                    val scope = rememberCoroutineScope()
                    BreedItem(
                        item = item,
                        onItemClick = { selectedItemName ->
                            scope.launch {
                                myList.clear()
                                myList.addAll(
                                    content.map { item ->
                                        item.selected = (item.name == selectedItemName)
                                        item
                                    }
                                )
                            }
                        }
                    )
                }
            }
//            Row (
//                modifier =  Modifier.horizontalScroll(rememberScrollState())
//            ){
//                StaggeredHorizontalGrid(
//                    numOfRows = 4,
//                    modifier = Modifier.padding(4.dp),
//                ) {
//                    items.forEach { breed ->
//                        BreedItem(breed)
//                    }
//                }
//            }
        }
    }
}

