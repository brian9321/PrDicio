package com.example.fotoapp.Flujocamera.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fotoapp.Flujocamera.Models.DatosPersonales
import com.example.fotoapp.Flujocamera.Models.Direccion
import com.example.fotoapp.Flujocamera.Models.ResponseData
import com.example.fotoapp.Flujocamera.Models.listOfUsers
import com.example.fotoapp.Flujocamera.Repository.UserRepository
import com.example.fotoapp.utils.ApiException
import com.example.fotoapp.utils.Event
import com.example.fotoapp.utils.NoInternetException
import com.example.fotoapp.viewModel.ViewModelBase
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class UserViewModel (private val repository: UserRepository
) : ViewModelBase() {

    private val _datosPersonales = MutableLiveData<DatosPersonales>()
    val datosPersonales: LiveData<DatosPersonales> get() = _datosPersonales

    private val _datosDireccion = MutableLiveData<Direccion>()
    val datosDireccion: LiveData<Direccion> get() = _datosDireccion

    val send_User = MutableLiveData<Event<ResponseData>>()

    val _image = MutableLiveData("")
    val image: LiveData<String> get() = _image

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    fun resetLoading() {
        _isLoading.value = false
    }
    fun setDatosPersonales(datos: DatosPersonales) {
        _datosPersonales.value = datos
    }

    fun setDatosDireccion(datos: Direccion) {
        _datosDireccion.value = datos
    }
    fun setImage(image: String) {
        _image.value = image
    }
    fun actualizarImagenNueva(imagenNueva: String) {
        val direccionActual = datosDireccion.value ?: return
        val direccionActualizada = direccionActual.copy(imagen = imagenNueva)
        _datosDireccion.value = direccionActualizada
    }

    fun actualizarDataGson() {
        val datosActual = datosPersonales.value ?: return
        val direccionActualizada = datosActual.copy(datos = Gson().toJson(datosDireccion.value))
        _datosPersonales.value = direccionActualizada
    }
    val getUsers = MutableLiveData<Event<List<ResponseData>>>()
    suspend fun sendUser(){
        try {
            setLoading(true)
            withContext(Dispatchers.IO) {
                send_User.postValue(Event(repository.createUser(datosPersonales.value!!)))
            }
        } catch (e: ApiException) {
            Log.e("error", "ApiException")
            resetLoading()
            errorMessage.value = Event(e.message!!)
            println(e.message)
        } catch (e: NoInternetException) {
            Log.e("error", "NoInternetException")
            resetLoading()
            errorMessage.value = Event(e.message!!)
            println(e.message)
        } catch (e: SocketTimeoutException) {
            Log.e("error", "SocketTimeoutException")
            resetLoading()
            errorMessage.value =
                Event("Tuvimos un problema, intente de nuevo mas tarde.")
        } finally {
            resetLoading()
        }
    }

    suspend fun getUsers(){
        try {
            setLoading(true)
            withContext(Dispatchers.IO) {
                getUsers.postValue(Event(repository.getUsers()))
            }
        } catch (e: ApiException) {
            resetLoading()
            errorMessage.value = Event(e.message!!)
            println(e.message)
        } catch (e: NoInternetException) {
            resetLoading()
            errorMessage.value = Event(e.message!!)
            println(e.message)
        } catch (e: SocketTimeoutException) {
            resetLoading()
            errorMessage.value =
                Event("Tuvimos un problema, intente de nuevo mas tarde.")
        }finally {
            resetLoading()
        }
    }

}