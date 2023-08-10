package com.example.fotoapp.Flujocamera

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.fotoapp.Flujocamera.Adapters.UserAdapter
import com.example.fotoapp.Flujocamera.Factory.UserViewModelFactory
import com.example.fotoapp.Flujocamera.Models.ResponseData
import com.example.fotoapp.Flujocamera.ViewModel.UserViewModel
import com.example.fotoapp.R
import com.example.fotoapp.databinding.FragmentListUsersBinding
import com.example.fotoapp.databinding.FragmentRegisterUserBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ListUsersFragment : Fragment(), KodeinAware, UserAdapter.OnPedidoClickListener {

    private lateinit var userAdapter: UserAdapter
    override val kodein by kodein()
    private lateinit var viewBinding: FragmentListUsersBinding

    private val factory: UserViewModelFactory by instance()
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this, factory).get()
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_options -> {
                view?.findNavController()?.navigate(R.id.action_listUsersFragment_to_registerUserFragment)
            }
            R.id.action_update -> {
                getDataUser()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(viewBinding.offerInfoToolbar)
        setHasOptionsMenu(true)

        getDataUser()
        viewModel.getUsers.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { users ->
                if(users != null){
                    userAdapter = UserAdapter(users, this)
                    viewBinding.recyclerUsers.adapter = userAdapter
                }

            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            viewBinding.loadingOverlay.visibility = View.VISIBLE
            viewBinding.progressBar.visibility = View.VISIBLE
        } else {
            viewBinding.loadingOverlay.visibility = View.GONE
            viewBinding.progressBar.visibility = View.GONE
        }
    }

    fun getDataUser(){
        lifecycleScope.launch(Dispatchers.Main + SupervisorJob()){
            viewModel.getUsers()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentListUsersBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onPedidoClick(user: ResponseData) {
        Log.e("USUARIO: ", "User $user")
    }

}