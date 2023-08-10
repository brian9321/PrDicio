package com.example.fotoapp.Flujocamera.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fotoapp.Flujocamera.Models.DatosPersonales
import com.example.fotoapp.Flujocamera.Models.Direccion
import com.example.fotoapp.Flujocamera.Repository.UserRepository
import com.example.fotoapp.models.ResponseEmpty
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

    val send_User = MutableLiveData<Event<ResponseEmpty>>()

    val _image = MutableLiveData("")
    val image: LiveData<String> get() = _image
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

    suspend fun sendUser(){
        try {
            withContext(Dispatchers.IO) {
                send_User.postValue(Event(repository.createUser(datosPersonales.value!!)))
            }
        } catch (e: ApiException) {
            Log.e("error", "ApiException")
            isBusy.postValue(Event(false))
            errorMessage.value = Event(e.message!!)
            println(e.message)
        } catch (e: NoInternetException) {
            Log.e("error", "NoInternetException")
            isBusy.postValue(Event(false))
            errorMessage.value = Event(e.message!!)
            println(e.message)
        } catch (e: SocketTimeoutException) {
            Log.e("error", "SocketTimeoutException")
            isBusy.postValue(Event(false))
            errorMessage.value =
                Event("Tuvimos un problema, intente de nuevo mas tarde.")
        }
    }

}