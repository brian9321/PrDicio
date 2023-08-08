package com.example.fotoapp.utils

import com.example.fotoapp.models.ResponseEmpty
import java.io.IOException

class ApiException(message: ResponseEmpty): IOException(message.message)
class NoInternetException(messge: String) : IOException(messge)