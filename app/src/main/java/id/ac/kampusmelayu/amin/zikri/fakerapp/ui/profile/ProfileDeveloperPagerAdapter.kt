package id.ac.kampusmelayu.amin.zikri.fakerapp.ui.profile

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ProfileDeveloperPagerAdapter(parent: Fragment) : FragmentStateAdapter(parent) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DevAboutFragment()
            1 -> DevContactFragment()
            else -> DevAboutFragment()
        }
    }
}
