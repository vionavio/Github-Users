package com.vionavio.consumerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.with
import com.vionavio.consumerapp.R
import com.vionavio.consumerapp.model.User
import kotlinx.android.synthetic.main.item_list.view.*

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.VHolder>() {
    var list : MutableList<User> = mutableListOf()
        set(mData) {
            if (mData.size > 0) {
                this.list.clear()
            }
            this.list.addAll(mData)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return VHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class VHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User?) {
            with(itemView) {
                with(context)
                    .load(user?.avatar)
                    .circleCrop()
                    .placeholder(android.R.color.darker_gray)
                    .error(android.R.color.darker_gray)
                    .into(itemView.component_avatar)
                itemView.component_name.text = user?.username
            }
        }
    }
}