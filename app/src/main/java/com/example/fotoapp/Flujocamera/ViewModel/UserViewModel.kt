package com.example.fotoapp.Flujocamera.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fotoapp.Flujocamera.Repository.UserRepository
import com.example.fotoapp.viewModel.ViewModelBase

class UserViewModel (private val repository: UserRepository
) : ViewModelBase() {

    val _name = MutableLiveData("")
    val name: LiveData<String> get() = _name

    fun setNameValue(nameValue: String) {
        _name.value = nameValue
    }

}