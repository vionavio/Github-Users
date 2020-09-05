package com.vionavio.githubuser.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.vionavio.githubuser.adapter.ComponentAdapter
import com.vionavio.githubuser.R
import com.vionavio.githubuser.model.User
import kotlinx.android.synthetic.main.fragment_component.view.*


class ComponentFragment : Fragment(){

    private var userList: ArrayList<User>? = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_component, container, false)

        arguments.let {
            userList = it?.getParcelableArrayList(PARAMS)
        }


        view.rv_list_item.layoutManager = LinearLayoutManager(requireContext())
        view.rv_list_item.itemAnimator = DefaultItemAnimator()
        view.rv_list_item.adapter =
            userList?.let {
                ComponentAdapter(it)
            }

        return view
    }



    companion object {
        fun newInstance( array: ArrayList<User>): ComponentFragment {
            return ComponentFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(PARAMS, array)
                }
            }
        }
        const val PARAMS = "params"
    }


}