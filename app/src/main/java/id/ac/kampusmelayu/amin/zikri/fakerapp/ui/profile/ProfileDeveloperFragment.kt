package id.ac.kampusmelayu.amin.zikri.fakerapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import id.ac.kampusmelayu.amin.zikri.fakerapp.R
import id.ac.kampusmelayu.amin.zikri.fakerapp.databinding.FragmentProfileDeveloperBinding

class ProfileDeveloperFragment : Fragment() {

    private var _binding: FragmentProfileDeveloperBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileDeveloperBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagerAdapter = ProfileDeveloperPagerAdapter(this)
        binding.viewpagerDev.adapter = pagerAdapter

        val tabTitles = arrayOf(
            getString(R.string.tab_about),

            getString(R.string.tab_contact)
        )

        TabLayoutMediator(binding.tabsDev, binding.viewpagerDev) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
