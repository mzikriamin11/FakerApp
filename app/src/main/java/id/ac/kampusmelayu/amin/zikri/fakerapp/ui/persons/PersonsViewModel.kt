package id.ac.kampusmelayu.amin.zikri.fakerapp.ui.persons

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.ac.kampusmelayu.amin.zikri.fakerapp.data.model.Person
import id.ac.kampusmelayu.amin.zikri.fakerapp.data.model.PersonsResponse
import id.ac.kampusmelayu.amin.zikri.fakerapp.data.remote.ApiConfig
import id.ac.kampusmelayu.amin.zikri.fakerapp.util.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonsViewModel : ViewModel() {

    companion object {
        private const val TAG = "PersonsViewModel"
    }

    // Source data terakhir dari API (sebelum filter)
    private var allPersons: List<Person> = emptyList()

    // Data yang ditampilkan di RecyclerView (setelah filter + search)
    private val _displayedPersons = MutableLiveData<List<Person>>(emptyList())
    val displayedPersons: LiveData<List<Person>> = _displayedPersons

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    // Filter state
    private var currentGenderFilter: String = Constants.GENDER_ALL
    private var currentSearchQuery: String = ""

    fun loadPersons(quantity: Int, defaultGender: String) {
        _isLoading.value = true
        _errorMessage.value = null
        currentGenderFilter = defaultGender

        // Untuk fleksibilitas: ambil semua data tanpa filter gender di server,
        // lalu filter di client agar bisa switch dengan bottom navigation
        // tanpa request ulang.
        val client = ApiConfig.getApiService().getPersons(
            quantity = quantity,
            locale = Constants.LOCALE,
            gender = null
        )
        client.enqueue(object : Callback<PersonsResponse> {
            override fun onResponse(
                call: Call<PersonsResponse>,
                response: Response<PersonsResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        allPersons = body.data
                        applyFilters()
                    } else {
                        _errorMessage.value = "Response kosong"
                    }
                } else {
                    _errorMessage.value = "Error ${response.code()}: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<PersonsResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message ?: "Unknown error"
                Log.e(TAG, "onFailure: ${t.message}", t)
            }
        })
    }

    fun setGenderFilter(gender: String) {
        currentGenderFilter = gender
        applyFilters()
    }

    fun setSearchQuery(query: String) {
        currentSearchQuery = query.trim().lowercase()
        applyFilters()
    }

    private fun applyFilters() {
        var result = allPersons

        // Filter gender
        if (currentGenderFilter != Constants.GENDER_ALL) {
            result = result.filter { it.gender == currentGenderFilter }
        }

        // Filter search query (cari di nama, email, atau kota)
        if (currentSearchQuery.isNotEmpty()) {
            result = result.filter { person ->
                val nameMatch = person.fullName.lowercase().contains(currentSearchQuery)
                val emailMatch = person.email?.lowercase()?.contains(currentSearchQuery) == true
                val cityMatch = person.address?.city?.lowercase()?.contains(currentSearchQuery) == true
                nameMatch || emailMatch || cityMatch
            }
        }

        _displayedPersons.value = result
    }

    /** Akses data mentah (untuk halaman Statistics dari sumber yang sama). */
    fun getAllPersons(): List<Person> = allPersons
}
