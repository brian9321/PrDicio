package com.example.fotoapp.Flujocamera.Repository

import android.service.autofill.UserData
import com.example.fotoapp.Flujocamera.Api.WSUser
import com.example.fotoapp.models.ResponseEmpty
import com.example.fotoapp.models.SafeApiRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository ( private val api: WSUser): SafeApiRequest() {
    suspend fun createUser(
        user: UserData
    ): ResponseEmpty {
        return apiRequest {
            api.createUser (user)
        }
    }
}