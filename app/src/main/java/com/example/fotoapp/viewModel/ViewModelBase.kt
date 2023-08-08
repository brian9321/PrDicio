package com.example.fotoapp.viewModel

import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fotoapp.utils.Event

open class ViewModelBase: ViewModel(), Observable {
    val statusMessage = MutableLiveData<Event<String>>()
    val errorMessage = MutableLiveData<Event<String>>()
    val isBusy = MutableLiveData<Event<Boolean>>()
    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        TODO("Not yet implemented")
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        TODO("Not yet implemented")
    }


}