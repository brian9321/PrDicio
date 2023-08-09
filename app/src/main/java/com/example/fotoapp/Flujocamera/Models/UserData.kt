package com.example.fotoapp.Flujocamera.Models

data class DatosPersonales(
    val nombre: String,
    val apellidoPaterno: String,
    val apellidoMaterno: String,
    val edad: Int,
    val email: String,
    val fechaNac: String,
    val datos: Direccion
)

data class Direccion(
    val calle: String,
    val numero: String,
    val colonia: String,
    val delegacionMunicipio: String,
    val estado: String,
    val cp: String,
    val imagen: String
)
