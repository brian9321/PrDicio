package com.example.fotoapp.models

import com.example.fotoapp.utils.ApiException
import com.google.gson.Gson
import retrofit2.Response
import java.net.SocketException

open class SafeApiRequest {
    var gson = Gson()

    suspend inline fun <reified T: Any> apiRequest(call: suspend ()-> Response<T>): T {
        val response = call.invoke()

        return if(response.isSuccessful){
            return response.body()!!
        } else {
            var responseFormat = gson.fromJson(
                response.errorBody()?.charStream(),
                ResponseEmpty::class.java
            )

            when(response.code()){
                400 -> {
                    throw ApiException(
                        responseFormat
                    )
                }
                401 -> {
                    throw ApiException(ResponseEmpty(code = 401))
                }
                else -> {
                    throw SocketException()
                }
            }
        }
    }
}