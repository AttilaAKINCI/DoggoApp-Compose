package com.akinci.doggoappcompose.ui.feaute.dashboard.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.doggoappcompose.common.coroutine.CoroutineContextProvider
import com.akinci.doggoappcompose.common.helper.state.ListState
import com.akinci.doggoappcompose.common.network.NetworkChecker
import com.akinci.doggoappcompose.common.network.NetworkResponse
import com.akinci.doggoappcompose.data.local.dao.BreedDao
import com.akinci.doggoappcompose.data.local.dao.SubBreedDao
import com.akinci.doggoappcompose.data.mapper.convertToBreedListEntity
import com.akinci.doggoappcompose.data.mapper.convertToSubBreedListEntity
import com.akinci.doggoappcompose.data.repository.DoggoRepository
import com.akinci.doggoappcompose.ui.feaute.dashboard.data.Breed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val coroutineContext: CoroutineContextProvider,
    private val doggoRepository: DoggoRepository,
    private val breedDao: BreedDao,
    private val subBreedDao: SubBreedDao,
    val networkChecker: NetworkChecker
): ViewModel() {

    var selectedBreedName: String = ""
    var selectedSubBreedName: String = ""

    // pass data to composable ui via states
    var breedListState by mutableStateOf(listOf<Breed>())
        private set

    var subBreedListState by mutableStateOf(listOf<Breed>())
        private set

    init {
        Timber.d("DashboardViewModel created..")
        getBreedList()
    }

    /** works like send and forget **/
//    private var _uiState = MutableStateFlow<UIState>(UIState.None)
//    var uiState: StateFlow<UIState> = _uiState

    fun selectBreed(breedName: String){
        if(breedName != selectedBreedName){
            selectedBreedName = breedName

            selectedSubBreedName = ""       // clear sub breed selection
            subBreedListState = listOf()    // clear sub breed list
            getSubBreedList(breedName)      // fetch new sub breed list
        }
    }
    fun selectSubBreed(subBreedName: String){
        selectedSubBreedName = subBreedName
    }

    private fun getBreedList() {
        if(breedListState.isEmpty()){
            viewModelScope.launch(coroutineContext.IO) {
                doggoRepository.getBreedList().collect { networkResponse ->
                    when(networkResponse){
                        is NetworkResponse.Loading -> {
                           // _breedListData.emit(ListState.OnLoading)
                        }
                        is NetworkResponse.Error -> {
                        //    _uiState.emit(UIState.OnServiceError)
                        }
                        is NetworkResponse.Success -> {
                            networkResponse.data?.let {
                                it.message.keys.map { item -> Breed(item) } // service response mapped Breed object
                                    .apply {
                                        // saves fetched data to room db
                                        breedDao.insertBreed(convertToBreedListEntity())
                                        breedListState = this
                                    }
                            }
                        }
                    }
                }
            }
        }
    }

    fun getSubBreedList(breed: String) {
        viewModelScope.launch(coroutineContext.IO) {
            doggoRepository.getSubBreedList(breed).collect { networkResponse ->
                when(networkResponse){
                    is NetworkResponse.Loading -> {
                        //_subBreedListData.emit(ListState.OnLoading)
                    }
                    is NetworkResponse.Error -> {
                    //    _uiState.emit(UIState.OnServiceError)
                    }
                    is NetworkResponse.Success -> {
                        networkResponse.data?.let {
                            it.message.map { item -> Breed(item)}.apply {
                                // saves fetched data to room db
                                subBreedDao.insertSubBreed(convertToSubBreedListEntity(ownerBreed = breed))
                                subBreedListState = this
                            }
                        }
                    }
                }
            }
        }
    }
}