package id.ac.kampusmelayu.amin.zikri.fakerapp.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import id.ac.kampusmelayu.amin.zikri.fakerapp.R
import id.ac.kampusmelayu.amin.zikri.fakerapp.data.model.Person
import id.ac.kampusmelayu.amin.zikri.fakerapp.data.repository.FavoritesRepository
import id.ac.kampusmelayu.amin.zikri.fakerapp.databinding.ActivityPersonDetailBinding
import id.ac.kampusmelayu.amin.zikri.fakerapp.databinding.RowDetailItemBinding
import id.ac.kampusmelayu.amin.zikri.fakerapp.util.Constants

class PersonDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonDetailBinding
    private var person: Person? = null
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup toolbar dengan back button
        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbarDetail.setNavigationOnClickListener { finish() }

        // Parse data person dari intent
        val json = intent.getStringExtra(Constants.EXTRA_PERSON_JSON)
        if (json.isNullOrEmpty()) {
            finish()
            return
        }
        person = Gson().fromJson(json, Person::class.java)
        person?.let { populateUi(it) }

        // Setup menu listener


        // Tombol buka maps
        binding.btnOpenMap.setOnClickListener {
            person?.address?.let { addr ->
                val lat = addr.latitude
                val lng = addr.longitude
                if (lat != null && lng != null) {
                    val label = person?.fullName
                    val uri = Uri.parse("geo:$lat,$lng?q=$lat,$lng($label)")
                    val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                    
                    try {
                        startActivity(mapIntent)
                    } catch (_: Exception) {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.error_no_maps_app),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.error_no_coordinates),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // Sync favorite icon awal

    }

    private fun populateUi(p: Person) {
        // Header
        binding.tvNameHeader.text = p.fullName
        Glide.with(this)
            .load(p.image)
            .placeholder(R.drawable.ic_person)
            .error(R.drawable.ic_person)
            .into(binding.imgAvatarDetail)

        // Personal info
        bindRow(binding.rowBirthday, R.drawable.ic_cake, getString(R.string.label_birthday), p.birthday)
        bindRow(binding.rowGender, R.drawable.ic_person, getString(R.string.label_gender), p.gender)

        // Contact
        bindRow(binding.rowEmail, R.drawable.ic_email, getString(R.string.label_email), p.email)
        bindRow(binding.rowPhone, R.drawable.ic_phone, getString(R.string.label_phone), p.phone)
        bindRow(binding.rowWebsite, R.drawable.ic_web, getString(R.string.label_website), p.website)

        // Address
        val addr = p.address
        bindRow(binding.rowStreet, R.drawable.ic_map, getString(R.string.label_street), addr?.streetName ?: addr?.street)
        bindRow(binding.rowBuilding, R.drawable.ic_map, getString(R.string.label_building), addr?.buildingNumber)
        bindRow(binding.rowCity, R.drawable.ic_map, getString(R.string.label_city), addr?.city)
        bindRow(binding.rowZipcode, R.drawable.ic_map, getString(R.string.label_zipcode), addr?.zipcode)
        bindRow(binding.rowCountry, R.drawable.ic_map, getString(R.string.label_country), addr?.country)
        val coords = if (addr?.latitude != null && addr.longitude != null) {
            "${addr.latitude}, ${addr.longitude}"
        } else null
        bindRow(binding.rowCoordinates, R.drawable.ic_map, getString(R.string.label_coordinates), coords)
    }

    /**
     * Mengisi sebuah baris detail (include layout row_detail_item).
     */
    private fun bindRow(rowBinding: RowDetailItemBinding, iconRes: Int, label: String, value: String?) {
        rowBinding.ivIcon.setImageResource(iconRes)
        rowBinding.tvLabel.text = label
        rowBinding.tvValue.text = if (value.isNullOrEmpty()) "-" else value
    }



    private fun sharePerson(p: Person) {
        val shareText = buildString {
            append("👤 ${p.fullName}\n")
            append("✉️ ${p.email ?: "-"}\n")
            append("📞 ${p.phone ?: "-"}\n")
            append("🎂 ${p.birthday ?: "-"}\n\n")
            append("📍 Alamat:\n")
            val addr = p.address
            append("${addr?.streetName ?: "-"} No. ${addr?.buildingNumber ?: "-"}\n")
            append("${addr?.city ?: "-"}, ${addr?.country ?: "-"} ${addr?.zipcode ?: ""}\n\n")
            append("Dibagikan dari FakerApp")
        }
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject))
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        startActivity(Intent.createChooser(intent, getString(R.string.action_share)))
    }
}
