package com.example.fotoapp.Flujocamera

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fotoapp.R
import com.example.fotoapp.databinding.FragmentCameraBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein


class RegisterUserFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    lateinit var binding: FragmentCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }
}