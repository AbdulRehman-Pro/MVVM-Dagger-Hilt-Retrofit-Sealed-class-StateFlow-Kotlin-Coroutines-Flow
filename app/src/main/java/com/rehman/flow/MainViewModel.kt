package com.rehman.flow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rehman.flow.network.RemoteDataRepository
import com.rehman.flow.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val remoteDataRepository: RemoteDataRepository) :
    ViewModel() {

    var apiResponse: MutableLiveData<Resource<ResponseBody>> = MutableLiveData()

    var apiStateFlow: MutableStateFlow<Resource<ResponseBody>> = MutableStateFlow(Resource.Empty())
    fun getPosts() {
        viewModelScope.launch {
            apiResponse.value = Resource.Loading()
            remoteDataRepository.getPost().collect {
                apiStateFlow.value = it
            }
        }
    }
}