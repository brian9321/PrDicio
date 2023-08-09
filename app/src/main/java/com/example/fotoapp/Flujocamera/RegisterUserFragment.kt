package com.example.fotoapp.Flujocamera

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.findNavController
import com.example.fotoapp.Flujocamera.Factory.UserViewModelFactory
import com.example.fotoapp.Flujocamera.ViewModel.UserViewModel
import com.example.fotoapp.R
import com.example.fotoapp.databinding.FragmentRegisterUserBinding
import com.google.android.material.textfield.TextInputEditText
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
class RegisterUserFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private lateinit var viewBinding: FragmentRegisterUserBinding

    private val factory: UserViewModelFactory by instance()
    private lateinit var viewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity.run {
            ViewModelProvider(this@RegisterUserFragment, factory).get()
        } ?: throw Exception("invalid activ"))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.btnDatos.setOnClickListener {
            viewDatos()
        }
        viewBinding.btnInfo.setOnClickListener {
            viewInfo()
        }

        viewBinding.btnCamera.setOnClickListener {
            view.findNavController().navigate(R.id.action_registerUserFragment_to_cameraFragment)
        }

        /*viewBinding.txtNombre.setOnFocusChangeListener { _, b ->
                if (!b) {
                    Log.e("ESTA ENTRANDO", "entro si")
                } else {
                    Log.e("ESTA ENTRANDO", "EN EL no")
                }
            }*/

        EditTextFocusChangeValidation(
            viewBinding.txtNombre,
            { input -> input.isEmpty() }, // Aquí define tu validación para el enfoque
            "Este campo es requerido"
        )
    }

    fun viewDatos(){
        viewBinding.layoutInfo.visibility = View.GONE
        viewBinding.layoutDatos.visibility = View.VISIBLE
    }

    fun viewInfo(){
        viewBinding.layoutDatos.visibility = View.GONE
        viewBinding.layoutInfo.visibility = View.VISIBLE
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
                    editText.error = null
                }
            } else {
                editText.error = null
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentRegisterUserBinding.inflate(inflater, container, false)
        return viewBinding.root
    }


}