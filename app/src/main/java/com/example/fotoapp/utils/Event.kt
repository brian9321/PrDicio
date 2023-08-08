package com.example.fotoapp.utils

open class Event<out T> (private val content: T){
    var hasBeenHabndled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if(hasBeenHabndled){
            null
        } else {
            hasBeenHabndled = true
            content
        }
    }

    fun peekContent(): T = content
}