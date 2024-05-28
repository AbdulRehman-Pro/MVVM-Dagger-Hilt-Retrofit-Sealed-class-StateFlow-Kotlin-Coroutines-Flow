package com.rehman.flow.network

import com.rehman.flow.baseClasses.BaseApiConverter
import com.rehman.flow.utils.AppConstants.ApiTypes.GetPosts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoteDataRepository @Inject constructor(private val apiService: ApiService) :
    BaseApiConverter() {

    suspend fun getPost() = flow {
        emit(safeApiCall({ apiService.getPost() }, apiName = GetPosts.name))
    }.flowOn(Dispatchers.IO)

}