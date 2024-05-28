package com.rehman.flow.network

import com.rehman.flow.models.PostModel
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("posts")
    suspend fun getPost() : Response<ResponseBody>
}