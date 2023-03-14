package com.o7services.recyclercrud

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.o7services.recyclercrud.databinding.ItemRvLayoutBinding

class UserAdapter(val userList:ArrayList<UserModel>,val list: ClickList):RecyclerView.Adapter<UserAdapter.viewHolder>() {
    class viewHolder(val binding: ItemRvLayoutBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding=ItemRvLayoutBinding.inflate(LayoutInflater.from(parent?.context),parent,false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.binding.tvName.text=userList[position].name
        holder.binding.tvRollNo.text=userList[position].mobile
        holder.itemView.setOnClickListener {
            list.listClicked(position)

        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}