package id.ac.kampusmelayu.amin.zikri.fakerapp.data.model

import com.google.gson.annotations.SerializedName

data class Address(
    @field:SerializedName("id")
    val id: Int = 0,

    @field:SerializedName("street")
    val street: String? = null,

    @field:SerializedName("streetName")
    val streetName: String? = null,

    @field:SerializedName("buildingNumber")
    val buildingNumber: String? = null,

    @field:SerializedName("city")
    val city: String? = null,

    @field:SerializedName("zipcode")
    val zipcode: String? = null,

    @field:SerializedName("country")
    val country: String? = null,

    @field:SerializedName("county_code")
    val countyCode: String? = null,

    @field:SerializedName("latitude")
    val latitude: Double? = null,

    @field:SerializedName("longitude")
    val longitude: Double? = null
)
