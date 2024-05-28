package com.rehman.flow.baseClasses

import android.util.Log
import com.rehman.flow.network.Resource
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response

abstract class BaseApiConverter {

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> Response<T>,
        apiName: String? = null,
    ): Resource<T> {

        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                body.let {
                    return Resource.Success(it, apiType = apiName)
                }
            }
            val message = response.errorBody()?.charStream()?.readText().toString()
            return Resource.Error(t = getErrorMessage(message), apiType = apiName)

        } catch (e: Exception) {
            return Resource.Error(t = e.localizedMessage, apiType = apiName)
        }

    }


    private fun getErrorMessage(apiResponse: String?): String? {
        try {
            val jObjError = JSONObject(apiResponse!!)
            when (val errorData = jObjError["message"]) {
                is JSONArray -> {

                    // It's an array
                    val jsonArray = jObjError.getJSONArray("message")
                    var message = ""

                    for (i in 0 until jsonArray.length()) {
                        message = if (i == 0)
                            jsonArray.getString(i)
                        else
                            message + "\n" + jsonArray.getString(i)
                    }

                    return message
                }

                is String -> {
                    // It's an object
                    return errorData.toString()
                }

                else -> return "Something went wrong Try again."
            }
        } catch (e: Exception) {
            Log.wtf(TAG, "getErrorMessage: " + e.localizedMessage)
            return apiResponse
        }
    }

    companion object {
        const val TAG = "BaseApiConverter"
    }

}