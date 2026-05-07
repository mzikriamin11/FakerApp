package id.ac.kampusmelayu.amin.zikri.fakerapp.data.model

import com.google.gson.annotations.SerializedName

data class PersonsResponse(
    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("code")
    val code: Int = 0,

    @field:SerializedName("total")
    val total: Int = 0,

    @field:SerializedName("data")
    val data: List<Person> = emptyList()
)
