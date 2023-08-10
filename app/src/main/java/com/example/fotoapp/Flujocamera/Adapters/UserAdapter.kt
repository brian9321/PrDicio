package com.example.fotoapp.Flujocamera.Adapters

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fotoapp.Flujocamera.Models.ResponseData
import com.example.fotoapp.R
import com.example.fotoapp.databinding.ItemUsersBinding

class UserAdapter (
    private val users: List<ResponseData>,
    private val listener: OnPedidoClickListener
): RecyclerView.Adapter<UserAdapter.UsersViewHolder>() {
    interface OnPedidoClickListener {
        fun onPedidoClick(user: ResponseData)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        UsersViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_users,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: UserAdapter.UsersViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    inner class UsersViewHolder(
        var itemUsersBinding: ItemUsersBinding
    ): RecyclerView.ViewHolder(itemUsersBinding.root), View.OnClickListener {

        private lateinit var user: ResponseData
        @SuppressLint("SetTextI18n")
        fun bind(userItem: ResponseData){
            this.user = userItem
            with(itemUsersBinding.root){
                if(userItem.nombre != null){
                    itemUsersBinding.txtName.text = "${userItem.nombre} ${userItem.apellidoPaterno}"
                }
                if(userItem.datos != null){
                    if(userItem.datos.imagen != null){
                        val startIndex = userItem.datos.imagen.indexOf(',') + 1
                        val base64Image = userItem.datos.imagen.substring(startIndex)
                        val decodedBytes = Base64.decode(base64Image, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                        itemUsersBinding.imgUser.setImageBitmap(bitmap)
                    }


                }

            }
        }

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
            listener.onPedidoClick(user)
        }
    }
}