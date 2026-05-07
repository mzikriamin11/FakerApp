package id.ac.kampusmelayu.amin.zikri.fakerapp.data.remote

import id.ac.kampusmelayu.amin.zikri.fakerapp.data.model.PersonsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    /**
     * Mengambil data persons dari fakerapi.it
     *
     * @param quantity Jumlah data yang akan diambil
     * @param locale Locale data (mis. "id_ID")
     * @param gender Optional gender filter (male/female), null = semua
     */
    @GET("api/v2/persons")
    fun getPersons(
        @Query("_quantity") quantity: Int,
        @Query("_locale") locale: String,
        @Query("_gender") gender: String? = null
    ): Call<PersonsResponse>
}
