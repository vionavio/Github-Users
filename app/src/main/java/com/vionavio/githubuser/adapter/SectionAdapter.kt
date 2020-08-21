package com.vionavio.githubuser.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SectionAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentSection = mutableListOf<Fragment>()
    private val titleSection = mutableListOf<String>()

    override fun getPageTitle(position: Int): CharSequence? = titleSection[position]

    override fun getItem(position: Int): Fragment = fragmentSection[position]

    override fun getCount(): Int = fragmentSection.size

    fun addFragment(fragment: Fragment, title: String){
        fragmentSection.add(fragment)
        titleSection.add(title)
        notifyDataSetChanged()
    }
}