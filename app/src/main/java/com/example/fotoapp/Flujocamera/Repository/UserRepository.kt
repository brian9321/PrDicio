package com.example.fotoapp.Flujocamera.Repository

import com.example.fotoapp.Flujocamera.Api.WSUser
import com.example.fotoapp.Flujocamera.Models.DatosPersonales
import com.example.fotoapp.models.ResponseEmpty
import com.example.fotoapp.models.SafeApiRequest

class UserRepository ( private val api: WSUser): SafeApiRequest() {
    suspend fun createUser(
        user: DatosPersonales
    ): ResponseEmpty {
        return apiRequest {
            api.createUser (user)
        }
    }
}