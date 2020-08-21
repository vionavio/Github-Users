package com.vionavio.githubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vionavio.githubuser.R
import com.vionavio.githubuser.model.User
import com.vionavio.githubuser.util.GlideApp
import kotlinx.android.synthetic.main.item_list.view.*

class ComponentAdapter(
    private val list: MutableList<User> = mutableListOf(),
    private val onClick: ((User) -> Unit)? = null
) :
    RecyclerView.Adapter<ComponentAdapter.VHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return VHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.bind(list[position])
    }

    fun addAll(result: List<User>?) {
        if (result != null) {
            list.clear()
            list.addAll(result)
            notifyDataSetChanged()
        }
    }

    inner class VHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User?) {
            with(itemView) {
                GlideApp.with(context)
                    .load(user?.avatar)
                    .circleCrop()
                    .placeholder(android.R.color.darker_gray)
                    .error(android.R.color.darker_gray)
                    .into(itemView.component_avatar)
                itemView.component_name.text = user?.username
                itemView.setOnClickListener {
                    if (user != null) {
                        onClick?.invoke(user)
                    }
                }
            }
        }
    }
}