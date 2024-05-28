package com.tubewiki.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tubewiki.home.fragment.CommonProblemFragment

class ViewPagerFragmentStateAdapter(
    val fragmentList: ArrayList<CommonProblemFragment>,
    context: FragmentActivity
) : FragmentStateAdapter(context) {

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }


    override fun getItemCount(): Int {
        return fragmentList.size
    }

}