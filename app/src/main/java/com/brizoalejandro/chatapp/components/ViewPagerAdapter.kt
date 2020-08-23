package com.brizoalejandro.chatapp.components

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    var fragments: ArrayList<Fragment> = ArrayList()
    var titles: ArrayList<String> = ArrayList()

    fun addFragment(fragment: Fragment, title: String){
        fragments.add((fragment))
        titles.add((title))
    }


    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }
}