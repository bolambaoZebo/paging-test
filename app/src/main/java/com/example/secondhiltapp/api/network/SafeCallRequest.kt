package com.example.secondhiltapp.api.network

import androidx.paging.PagingSource
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.lang.Exception

abstract class SafeCallRequest {

    suspend fun <T: Any> apiRequest(call: suspend () -> Response<T>) : T {
        try {
            val response = call.invoke()
            if (response.isSuccessful){
                return response.body()!!
            }else{
                val error = response.errorBody()?.string()
                val message = StringBuilder()

                error?.let {
                    try {
                        message.append(JSONObject(it).getString("message"))
                    }catch (e: JSONException){ }
                }

                message.append("Error Code: ${response.code()}: ${error}")
                throw Exception(message.toString())
            }

        } catch (exception: IOException) {
            throw Exception(exception.message)
        } catch (exception: HttpException) {
            throw Exception(exception.message)
        }
    }
}