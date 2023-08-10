package com.example.fotoapp

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.fotoapp.databinding.FragmentCameraBinding
import org.kodein.di.android.x.kodein
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import android.Manifest
import android.content.ContentValues
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageCaptureException
import androidx.lifecycle.ViewModelProvider
import com.example.fotoapp.Flujocamera.Factory.UserViewModelFactory
import com.example.fotoapp.Flujocamera.ViewModel.UserViewModel
import com.example.fotoapp.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executors

import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
class MainActivity : AppCompatActivity(), KodeinAware {


    private val CAMERA_PERMISSION_REQUEST_CODE = 101
    private val STORAGE_PERMISSION_REQUEST_CODE = 102

    private lateinit var viewBinding: ActivityMainBinding
    override val kodein by kodein()
    private lateinit var viewModel: UserViewModel
    private val factory: UserViewModelFactory by instance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)
        setContentView(viewBinding.root)
    }

    override fun onResume() {
        super.onResume()
        checkCameraPermission()
        checkStoragePermission()
    }

    // Verificar y solicitar permiso de cámara
    fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)

        } else {
            // Permiso de cámara ya concedido
            // Puedes realizar acciones que requieran acceso a la cámara aquí
        }
    }

    // Verificar y solicitar permiso de almacenamiento
    fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_REQUEST_CODE)
        } else {
            // Permiso de almacenamiento ya concedido
            // Puedes realizar acciones que requieran acceso a archivos aquí
        }
    }

    // Manejar el resultado de la solicitud de permisos
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso de cámara concedido, puedes realizar acciones que requieran acceso a la cámara aquí
            } else {
                checkCameraPermission()
            }
        } else if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso de almacenamiento concedido, puedes realizar acciones que requieran acceso a archivos aquí
            } else {
                checkStoragePermission()
            }
        }
    }


}