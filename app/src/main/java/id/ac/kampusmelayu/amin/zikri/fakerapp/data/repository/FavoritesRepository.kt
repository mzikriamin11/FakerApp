package id.ac.kampusmelayu.amin.zikri.fakerapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.ac.kampusmelayu.amin.zikri.fakerapp.data.model.Person

/**
 * Repository penyimpan favorit. Implementasi in-memory (data akan hilang
 * saat aplikasi dimatikan). Untuk implementasi yang persisten, dapat
 * digantikan dengan Room atau DataStore.
 */
object FavoritesRepository {

    private val _favorites = MutableLiveData<List<Person>>(emptyList())
    val favorites: LiveData<List<Person>> = _favorites

    fun add(person: Person) {
        val current = _favorites.value.orEmpty().toMutableList()
        if (current.none { it.id == person.id }) {
            current.add(person)
            _favorites.value = current
        }
    }

    fun remove(person: Person) {
        val current = _favorites.value.orEmpty().toMutableList()
        current.removeAll { it.id == person.id }
        _favorites.value = current
    }

    fun contains(person: Person): Boolean {
        return _favorites.value.orEmpty().any { it.id == person.id }
    }

    fun toggle(person: Person): Boolean {
        return if (contains(person)) {
            remove(person)
            false
        } else {
            add(person)
            true
        }
    }
}
