package br.edu.uea.ecopoints.screen.home.admin.fragment.report

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ClientFragment()
            1 -> ControlFragment()
            else -> ClientFragment()
        }
    }

}