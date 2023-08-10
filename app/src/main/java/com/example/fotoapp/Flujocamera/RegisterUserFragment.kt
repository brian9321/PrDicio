package com.example.fotoapp.Flujocamera

import android.os.Bundle
import android.service.autofill.UserData
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.fotoapp.Flujocamera.Factory.UserViewModelFactory
import com.example.fotoapp.Flujocamera.Models.DatosPersonales
import com.example.fotoapp.Flujocamera.Models.Direccion
import com.example.fotoapp.Flujocamera.ViewModel.UserViewModel
import com.example.fotoapp.R
import com.example.fotoapp.databinding.FragmentRegisterUserBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterUserFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private lateinit var viewBinding: FragmentRegisterUserBinding

    private val factory: UserViewModelFactory by instance()
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this, factory).get()
        } ?: throw Exception("Invalid Activity")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentRegisterUserBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnOnClick(view)

        viewBinding.txtNombre.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isValid = s?.matches("[a-zA-Z ]+".toRegex()) == true && s.isNotEmpty()
                viewBinding.txtNombre.error = if (isValid) null else "Ingresa un nombre válido"

            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        viewBinding.txtNombre.setupTextWatcher("Ingresa un nombre válido") { text ->
            isTextValid(text)
        }

        viewBinding.txtApellido.setupTextWatcher("Ingresa un apellido válido") { text ->
            isTextValid(text)
        }

        viewBinding.txtCorreo.setupEmailValidator()

        viewBinding.txtNum.setupNumberValidator()
        viewBinding.txtCP.setupNumberValidator()
        viewBinding.txtCalle.setupTextWatcher("Ingresa una calle válida") { text ->
            isTextValid(text)
        }
        viewBinding.txtColonia.setupTextWatcher("Ingresa una colonia válida") { text ->
            isTextEmpty(text)
        }
        viewBinding.txtDelMun.setupTextWatcher("Ingresa una colonia válida") { text ->
            isTextEmpty(text)
        }

        viewBinding.txtEstado.setupTextWatcher("Ingresa una calle válida") { text ->
            isTextValid(text)
        }
        viewBinding.txtFecha.setupDateValidator()

        viewBinding.txtNombre.addTextChangedListener { updateButtonState() }
        viewBinding.txtApellido.addTextChangedListener { updateButtonState() }
        viewBinding.txtApellidoMat.addTextChangedListener { updateButtonState() }
        viewBinding.txtCorreo.addTextChangedListener { updateButtonState() }
        viewBinding.txtFecha.addTextChangedListener { updateButtonState() }

        viewBinding.txtNum.addTextChangedListener { updateButtonFoto() }
        viewBinding.txtCalle.addTextChangedListener { updateButtonFoto() }
        viewBinding.txtEstado.addTextChangedListener { updateButtonFoto() }
        viewBinding.txtColonia.addTextChangedListener { updateButtonFoto() }
        viewBinding.txtCP.addTextChangedListener { updateButtonFoto() }
        viewBinding.txtDelMun.addTextChangedListener { updateButtonFoto() }
    }
    fun TextInputEditText.setupDateValidator() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No necesitas hacer nada aquí
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Verifica si el texto es una fecha válida en el formato AAAA-MM-DD
                val isValid = s?.matches("\\d{4}-\\d{2}-\\d{2}".toRegex()) == true

                // Activa o desactiva la validación según corresponda
                error = if (isValid) null else "Ingresa una fecha válida (AAAA-MM-DD)"
            }

            override fun afterTextChanged(s: Editable?) {
                // No necesitas hacer nada aquí
            }
        })
    }
    fun TextInputEditText.setupTextWatcher(errorMessage: String, validation: (CharSequence?) -> Boolean) {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isValid = validation(s)
                error = if (isValid) null else errorMessage
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
    fun TextInputEditText.setupNumberValidator() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isValid = s?.matches("[0-9]+".toRegex()) == true
                error = if (isValid) null else "Ingresa solo números"
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
    fun TextInputEditText.setupEmailValidator() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()
                error = if (isValid) null else "Ingresa un correo electrónico válido"
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
    fun updateButtonState() {
        viewBinding.btnDatos.isEnabled = areFieldsValid()
    }

    fun updateButtonFoto() {
        viewBinding.btnCamera.isEnabled = areFieldsValidData()
    }
    fun areFieldsValid(): Boolean {
        val nombreValid = viewBinding.txtNombre.text?.matches("[a-zA-Z ]+".toRegex()) == true && viewBinding.txtNombre.text?.isNotEmpty() == true
        val apellidoValid = viewBinding.txtApellido.text?.matches("[a-zA-Z ]+".toRegex()) == true && viewBinding.txtApellido.text?.isNotEmpty() == true
        val apellidoMatValid = viewBinding.txtApellidoMat.text?.matches("[a-zA-Z ]+".toRegex()) == true && viewBinding.txtApellidoMat.text?.isNotEmpty() == true
        val correoValid = viewBinding.txtCorreo.text?.matches("[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+".toRegex()) == true && viewBinding.txtCorreo.text?.isNotEmpty() == true
        //val numeroValid = viewBinding.txtNum.text?.matches("\\d+".toRegex()) == true && viewBinding.txtNum.text?.isNotEmpty() == true
        val fechaValid = viewBinding.txtFecha.text?.matches("\\d{4}-\\d{2}-\\d{2}".toRegex()) == true

        return nombreValid && apellidoValid && correoValid  && fechaValid && apellidoMatValid
    }

    fun areFieldsValidData(): Boolean {
        val calleValid = viewBinding.txtCalle.text?.matches("[a-zA-Z ]+".toRegex()) == true && viewBinding.txtCalle.text?.isNotEmpty() == true
        val numeroValid = viewBinding.txtNum.text?.matches("\\d+".toRegex()) == true && viewBinding.txtNum.text?.isNotEmpty() == true
        val estadoValid = viewBinding.txtEstado.text?.matches("[a-zA-Z ]+".toRegex()) == true && viewBinding.txtEstado.text?.isNotEmpty() == true
        val coloniaValid =  viewBinding.txtColonia.text?.isNotEmpty() == true
        val delValid =  viewBinding.txtDelMun.text?.isNotEmpty() == true
        val cpValid = viewBinding.txtCP.text?.matches("\\d+".toRegex()) == true && viewBinding.txtCP.text?.isNotEmpty() == true

        Log.e("calleValid",calleValid.toString())
        Log.e("numeroValid",numeroValid.toString())
        Log.e("estadoValid",estadoValid.toString())
        Log.e("coloniaValid",coloniaValid.toString())
        Log.e("delValid",delValid.toString())
        Log.e("cpValid",cpValid.toString())
        return calleValid && numeroValid && coloniaValid  && delValid && estadoValid && cpValid
    }
    fun isTextEmpty(text: CharSequence?): Boolean {
        return text!!.isNotEmpty()
    }
    fun isTextValid(text: CharSequence?): Boolean {
        return text?.matches("[a-zA-Z ]+".toRegex()) == true && text.isNotEmpty()
    }
    fun btnOnClick(view: View){

        viewBinding.btnDatos.setOnClickListener {
            viewDatos()
        }

        viewBinding.btnInfo.setOnClickListener {
            viewInfo()
        }

        viewBinding.btnCamera.setOnClickListener {
            sendData()
        }
    }

    private fun sendData(){

        val direccion = Direccion(
        viewBinding.txtCalle.text.toString().trim(),
        viewBinding.txtNum.text.toString().trim(),
        viewBinding.txtColonia.text.toString().trim(),
        viewBinding.txtDelMun.text.toString().trim(),
        viewBinding.txtEstado.text.toString().trim(),
        viewBinding.txtCP.text.toString().trim(),
        "img.png"
        )
        val user = DatosPersonales(
            viewBinding.txtNombre.text.toString().trim(),
            viewBinding.txtApellido.text.toString().trim(),
            viewBinding.txtApellidoMat.text.toString().trim(),
            calcularEdad(viewBinding.txtFecha.text.toString().trim()),
            viewBinding.txtCorreo.text.toString().trim(),
            viewBinding.txtFecha.text.toString().trim(),
            Gson().toJson(direccion)
        )
/*
        val direccionpr = Direccion(
            "sol",
            "12",
            "centro",
            "comonfort",
            "gto",
            "38210",
            "img.png"
        )
        val userpr = DatosPersonales(
            "Brian",
            "Aboytes",
            "Morales",
            26,
            "brayan932@gmail.com",
            "30/07/1997",
            Gson().toJson(direccionpr)
        )*/

        lifecycleScope.launch(Dispatchers.Main + SupervisorJob()){
            viewModel.setDatosPersonales(user)
        }

        viewModel.datosPersonales.observe(viewLifecycleOwner) {datosPersonales ->
            viewModel.setDatosDireccion(direccion)
        }

        viewModel.datosDireccion.observe(viewLifecycleOwner) {datosDireccion ->
            view?.findNavController()?.navigate(R.id.action_registerUserFragment_to_cameraFragment)
        }
    }
    fun viewDatos(){
        viewBinding.layoutInfo.visibility = View.GONE
        viewBinding.layoutDatos.visibility = View.VISIBLE
    }
    fun viewInfo(){
        viewBinding.layoutDatos.visibility = View.GONE
        viewBinding.layoutInfo.visibility = View.VISIBLE
    }

    fun calcularEdad(fechaNacimiento: String): Int {
        val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaNac = formato.parse(fechaNacimiento)
        val fechaActual = Calendar.getInstance().time

        val diff = fechaActual.time - fechaNac.time
        val edadMilisegundos = diff / (1000 * 60 * 60 * 24 * 365.25)

        return edadMilisegundos.toInt()
    }

}