package com.example.fotoapp.Flujocamera

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import com.example.fotoapp.Flujocamera.Factory.UserViewModelFactory
import com.example.fotoapp.Flujocamera.ViewModel.UserViewModel
import com.example.fotoapp.R
import com.example.fotoapp.databinding.FragmentRegisterUserBinding
import com.example.fotoapp.databinding.FragmentViewUsersBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ViewUsersFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private lateinit var binding: FragmentViewUsersBinding

    private val factory: UserViewModelFactory by instance()
    private lateinit var viewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this, factory).get()
        } ?: throw Exception("Invalid Activity")
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch(Dispatchers.Main + SupervisorJob()){
            val datosPersonales = viewModel.datosPersonales.value
            val datosDireccion = viewModel.datosDireccion.value
            binding.txvName.text = "Nombre: ${datosPersonales?.nombre} ${datosPersonales?.apellidoPaterno} ${datosPersonales?.apellidoMaterno}"
            binding.tvEdad.text = "Edad: ${datosPersonales?.edad} "
            binding.tvFechaNa.text = "Fecha Nacimiento: ${datosPersonales?.fechaNac} "
            binding.tvEmail.text = "Correo: ${datosPersonales?.email} "
            binding.tvDireccion.text = "Direccion: ${datosDireccion?.calle} #${datosDireccion?.numero},  ${datosDireccion?.colonia}, ${datosDireccion?.delegacion}, ${datosDireccion?.estado}"
            binding.tvCp.text = "CP: ${datosDireccion?.cp} "
            imageTransform()
        }

        binding.fabEditar.setOnClickListener{
            updateData()
        }
    }

    fun updateData(){
        lifecycleScope.launch(Dispatchers.Main + SupervisorJob()){
            viewModel.actualizarDataGson()
            val datosPersonales = viewModel.datosPersonales.value
            Log.e("DATOS PR FINAL", "Datos guardados: $datosPersonales")

            viewModel.sendUser()
        }
    }
    fun sendUser(){
        lifecycleScope.launch(Dispatchers.Main + SupervisorJob()){

        }
    }

    fun imageTransform(){
        val img = viewModel.image.value
        val imageData = Base64.decode(img, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
        binding.imvFoto.setImageBitmap(bitmap)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

}