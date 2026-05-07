package id.ac.kampusmelayu.amin.zikri.fakerapp.ui.persons

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import id.ac.kampusmelayu.amin.zikri.fakerapp.R
import id.ac.kampusmelayu.amin.zikri.fakerapp.data.preferences.SettingsManager
import id.ac.kampusmelayu.amin.zikri.fakerapp.databinding.FragmentPersonsBinding
import id.ac.kampusmelayu.amin.zikri.fakerapp.ui.detail.PersonDetailActivity
import id.ac.kampusmelayu.amin.zikri.fakerapp.ui.persons.adapter.PersonAdapter
import id.ac.kampusmelayu.amin.zikri.fakerapp.util.Constants

class PersonsFragment : Fragment(), MenuProvider {

    private var _binding: FragmentPersonsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PersonsViewModel by viewModels()
    private lateinit var adapter: PersonAdapter
    private lateinit var settingsManager: SettingsManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsManager = SettingsManager(requireContext())

        // Tambahkan menu (search, refresh) ke toolbar Activity
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        setupRecyclerView()
        setupBottomNav()
        setupSwipeRefresh()
        setupRetryButton()
        observeViewModel()

        // Load awal jika belum
        if (viewModel.getAllPersons().isEmpty()) {
            loadFromSettings()
        }
    }

    private fun setupRecyclerView() {
        adapter = PersonAdapter { person ->
            // Klik item → buka detail dengan kirim data via Gson
            val json = Gson().toJson(person)
            val intent = Intent(requireContext(), PersonDetailActivity::class.java)
            intent.putExtra(Constants.EXTRA_PERSON_JSON, json)
            startActivity(intent)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun setupBottomNav() {
        // Set initial selection sesuai default gender
        val initial = when (settingsManager.defaultGender) {
            Constants.GENDER_MALE -> R.id.bottom_male
            Constants.GENDER_FEMALE -> R.id.bottom_female
            else -> R.id.bottom_all
        }
        binding.bottomNav.selectedItemId = initial

        binding.bottomNav.setOnItemSelectedListener { item ->
            val gender = when (item.itemId) {
                R.id.bottom_male -> Constants.GENDER_MALE
                R.id.bottom_female -> Constants.GENDER_FEMALE
                else -> Constants.GENDER_ALL
            }
            viewModel.setGenderFilter(gender)
            true
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            loadFromSettings()
        }
    }

    private fun setupRetryButton() {
        binding.btnRetry.setOnClickListener { loadFromSettings() }
    }

    private fun loadFromSettings() {
        viewModel.loadPersons(
            quantity = settingsManager.quantity,
            defaultGender = settingsManager.defaultGender
        )
    }

    private fun observeViewModel() {
        viewModel.displayedPersons.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.emptyView.visibility = if (list.isEmpty() && !viewModel.isLoading.value!!) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            // Saat sedang refresh, hide progressBar (sudah ada indikator swipe)
            if (binding.swipeRefresh.isRefreshing) {
                if (!loading) binding.swipeRefresh.isRefreshing = false
            } else {
                binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            }
            if (loading) binding.emptyView.visibility = View.GONE
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { msg ->
            if (msg != null) {
                binding.emptyText.text = getString(R.string.error_load, msg)
                binding.emptyView.visibility = View.VISIBLE
            }
        }
    }

    // ----- Menu (search, refresh) -----
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_persons_top, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as? SearchView
        searchView?.queryHint = getString(R.string.hint_search)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setSearchQuery(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchQuery(newText.orEmpty())
                return true
            }
        })
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_refresh -> {
                loadFromSettings()
                true
            }
            else -> false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
