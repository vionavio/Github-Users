package com.vionavio.githubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.vionavio.githubuser.R
import com.vionavio.githubuser.model.User
import com.vionavio.githubuser.util.glide.GlideApp
import com.vionavio.githubuser.view.DetailActivity
import kotlinx.android.synthetic.main.item_list.view.*

class FavoriteAdapter(
    private val list: MutableList<User> = mutableListOf(),
    private val onClick: ((User) -> Unit)? = null,
    private val onLongClick: ((User, Int) -> Unit)? = null
) :
    RecyclerView.Adapter<FavoriteAdapter.VHolder>() {
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

    fun removeItem(position: Int) {
        this.list.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.list.size)
    }


    @Suppress("DEPRECATION")
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

                itemView.setOnLongClickListener {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle(R.string.alert)
                    builder.setMessage(R.string.message)

                    builder.setPositiveButton(R.string.yes) { _, _ ->
                        if (user != null) {
                            onLongClick?.invoke(user, adapterPosition)
                        }
                        Toast.makeText(context, R.string.yes, Toast.LENGTH_SHORT).show()
                    }
                    builder.setNegativeButton(R.string.no) { _, _ ->
                        Toast.makeText(context, R.string.no, Toast.LENGTH_SHORT).show()
                    }
                    builder.show()
                    return@setOnLongClickListener true
                }
            }
        }
    }
}