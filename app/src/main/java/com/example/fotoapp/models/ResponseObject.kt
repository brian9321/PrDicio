package com.example.fotoapp.models

class ResponseObject<T> (
    override val code: Int,
    override val message: String
) : IResponse {
    var data: T? =  null
}