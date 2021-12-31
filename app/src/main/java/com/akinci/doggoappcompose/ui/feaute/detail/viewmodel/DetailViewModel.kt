package com.akinci.doggoappcompose.ui.feaute.detail.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.doggoappcompose.common.coroutine.CoroutineContextProvider
import com.akinci.doggoappcompose.common.network.NetworkResponse
import com.akinci.doggoappcompose.common.helper.state.ListState
import com.akinci.doggoappcompose.common.helper.state.UIState
import com.akinci.doggoappcompose.data.local.dao.ContentDao
import com.akinci.doggoappcompose.data.mapper.convertToDoggoContentListEntity
import com.akinci.doggoappcompose.data.repository.DoggoRepository
import com.akinci.doggoappcompose.ui.feaute.dashboard.data.Breed
import com.akinci.doggoappcompose.ui.feaute.detail.data.Content
import com.akinci.doggoappcompose.ui.feaute.detail.helper.DoggoNameProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val coroutineContext: CoroutineContextProvider,
    private val doggoRepository: DoggoRepository,
    private val contentDao: ContentDao,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    // pass data to composable ui via states
    var breedImageListState by mutableStateOf(listOf<Content>())
        private set

    private val fragmentArgBreed by lazy { savedStateHandle.get("breed") ?: "" }
    private val fragmentArgSubBreed by lazy { savedStateHandle.get("subBreed") ?: "" }

    /** Network warning popup should seen once **/
    var isNetworkWarningDialogVisible by mutableStateOf(true)
        private set

    fun networkWarningSeen(){
        isNetworkWarningDialogVisible = false
    }

    init {
        Timber.d("DetailViewModel created..")

        getDoggoContent(fragmentArgBreed, fragmentArgSubBreed)
    }

    /** works like send and forget **/
    private var _uiState = MutableStateFlow<UIState>(UIState.None)
    var uiState: StateFlow<UIState> = _uiState

    fun getDoggoContent(breed: String, subBreed: String = "", count: Int = (10..30).random()) {
        viewModelScope.launch(coroutineContext.IO) {
            doggoRepository.getDoggoContent(breed = breed, subBreed = subBreed, count = count).collect { networkResponse ->
                when(networkResponse){
                    is NetworkResponse.Loading -> {
                        //_breedImageListData.emit(ListState.OnLoading)
                    }
                    is NetworkResponse.Error -> {
                    //    _uiState.emit(UIState.OnServiceError)
                    }
                    is NetworkResponse.Success -> {
                        networkResponse.data?.message?.let {
                            Timber.d("DetailViewModel:: Doggo content fetched..")
                            // saves fetched data to room db
                            Timber.d("DetailViewModel:: Doggo content is written to ROOM DB")
                            contentDao.insertContent(contentList = it.convertToDoggoContentListEntity(breed = breed, subBreed = subBreed))

                            Timber.d("DetailViewModel:: Doggo content is send as a state to UI")
                            breedImageListState = it.map { imageUrl ->
                                Content(
                                    imageUrl = imageUrl,
                                    dogName = DoggoNameProvider.getRandomName()
                                )
                            }
                        }
                    }
                }
            }
        }
    }


}