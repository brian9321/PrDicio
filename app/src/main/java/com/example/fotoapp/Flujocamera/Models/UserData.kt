package com.example.fotoapp.Flujocamera.Models

data class DatosPersonales(
    val nombre: String,
    val apellidoPaterno: String,
    val apellidoMaterno: String,
    val edad: Int,
    val email: String,
    val fechaNac: String,
    val datos: String // Cambiado a String para almacenar los datos como JSON
)

data class Direccion(
    val calle: String,
    val numero: String,
    val colonia: String,
    val delegacion: String,
    val estado: String,
    val cp: String,
    val imagen: String
)

data class listOfUsers(
    val listOfAccountsToTrade: List<ResponseData>? = null
)
data class ResponseData(
    val id: Int,
    val nombre: String?,
    val apellidoPaterno: String?,
    val apellidoMaterno: String?,
    val edad: Int?,
    val email: String?,
    val fechaNac: String?,
    val datos: Direccion?
)


