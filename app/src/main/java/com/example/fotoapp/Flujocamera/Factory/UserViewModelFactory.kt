package com.example.fotoapp.Flujocamera.Factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fotoapp.Flujocamera.Repository.UserRepository
import com.example.fotoapp.Flujocamera.ViewModel.UserViewModel

class UserViewModelFactory (
    private val repository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(repository) as T
    }
}