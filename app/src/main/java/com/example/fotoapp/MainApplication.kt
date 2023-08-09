package com.example.fotoapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.example.fotoapp.Flujocamera.Api.WSUser
import com.example.fotoapp.Flujocamera.Factory.UserViewModelFactory
import com.example.fotoapp.Flujocamera.Repository.UserRepository
import com.example.fotoapp.network.NetworkConnectionInterceptor
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class MainApplication : Application(), KodeinAware {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var contextApp: Context
    }

    override val kodein = Kodein.lazy {
        import(androidXModule(this@MainApplication))

        bind() from singleton { UserRepository(instance()) }
        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { WSUser(instance()) }
        bind() from singleton { UserViewModelFactory(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        contextApp = applicationContext
    }
}