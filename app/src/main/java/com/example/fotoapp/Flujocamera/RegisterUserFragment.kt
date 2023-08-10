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
        valData()
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
            //view.findNavController().navigate(R.id.action_registerUserFragment_to_cameraFragment)
        }
    }
    private fun setupEditTextValidation(
        editText: TextInputEditText,
        validation: (String) -> Boolean,
        errorMessage: String
    ) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = s.toString().trim()
                if (validation(input)) {
                    editText.error = errorMessage
                } else {
                    editText.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun EditTextFocusChangeValidation(
        editText: TextInputEditText,
        validation: (String) -> Boolean,
        errorMessage: String
    ) {
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val input = editText.text.toString().trim()
                if (validation(input)) {
                    editText.error = errorMessage
                } else {
                    editText.setError(null)
                }
            } else {
                editText.setError(null)
            }
        }
    }

    fun valData(){
        requiredDataMessage(viewBinding.txtNombre)
        requiredDataMessage(viewBinding.txtApellido)
        requiredDataMessage(viewBinding.txtApellidoMat)
        //EditTextValidationEmail(viewBinding.txtCorreo)
        requiredDataMessage(viewBinding.txtFecha)
        requiredDataMessage(viewBinding.txtCalle)
        requiredDataMessage(viewBinding.txtNum)
        requiredDataMessage(viewBinding.txtDelMun)
        requiredDataMessage(viewBinding.txtEstado)
        requiredDataMessage(viewBinding.txtCP)

        viewBinding.txtCorreo.setOnEditorActionListener { edit, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                Log.e("EDITAR", "EMAIL")
            }
            false
        }
    }
    private fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }
    fun EditTextValidationEmail(editText: TextInputEditText){

        EditTextFocusChangeValidation(
            editText,
            { input -> editText.text!!.trim().isNotEmpty()
                    && Patterns.EMAIL_ADDRESS.matcher(editText.text!!.trim())
                .matches()},
            "Correo electrónico inválido"
        )
    }
    fun requiredDataMessage(editText: TextInputEditText){
        EditTextFocusChangeValidation(
            editText,
            { input -> input.isEmpty() }, // Aquí define tu validación para el enfoque
            "Este campo es requerido"
        )
    }
    private fun sendData(){
        /*
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
            direccion
        )
*/
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
        )

        lifecycleScope.launch(Dispatchers.Main + SupervisorJob()){
            viewModel.setDatosPersonales(userpr)
        }

        viewModel.datosPersonales.observe(viewLifecycleOwner) {datosPersonales ->
            viewModel.setDatosDireccion(direccionpr)
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
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fechaNac = formato.parse(fechaNacimiento)
        val fechaActual = Calendar.getInstance().time

        val diff = fechaActual.time - fechaNac.time
        val edadMilisegundos = diff / (1000 * 60 * 60 * 24 * 365.25)

        return edadMilisegundos.toInt()
    }
}