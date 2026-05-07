package id.ac.kampusmelayu.amin.zikri.fakerapp.data.model

import com.google.gson.annotations.SerializedName

data class Person(
    @field:SerializedName("id")
    val id: Int = 0,

    @field:SerializedName("firstname")
    val firstname: String? = null,

    @field:SerializedName("lastname")
    val lastname: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("phone")
    val phone: String? = null,

    @field:SerializedName("birthday")
    val birthday: String? = null,

    @field:SerializedName("gender")
    val gender: String? = null,

    @field:SerializedName("address")
    val address: Address? = null,

    @field:SerializedName("website")
    val website: String? = null,

    @field:SerializedName("image")
    val image: String? = null
) {
    val fullName: String
        get() = listOfNotNull(firstname, lastname).joinToString(" ").ifEmpty { "(Tanpa Nama)" }
}
