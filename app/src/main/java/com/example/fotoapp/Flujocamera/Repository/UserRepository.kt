package com.example.fotoapp.Flujocamera.Repository

import com.example.fotoapp.Flujocamera.Api.WSUser
import com.example.fotoapp.Flujocamera.Models.DatosPersonales
import com.example.fotoapp.Flujocamera.Models.ResponseData
import com.example.fotoapp.Flujocamera.Models.listOfUsers
import com.example.fotoapp.models.SafeApiRequest

class UserRepository ( private val api: WSUser): SafeApiRequest() {
    suspend fun createUser(
        user: DatosPersonales
    ): ResponseData {
        return apiRequest {
            api.createUser (user)
        }
    }

    suspend fun getUsers(): List<ResponseData> {
        return apiRequest {
            api.getLatestUsers()
        }

    }
}