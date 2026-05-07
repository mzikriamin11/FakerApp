package id.ac.kampusmelayu.amin.zikri.fakerapp.ui.persons.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.ac.kampusmelayu.amin.zikri.fakerapp.R
import id.ac.kampusmelayu.amin.zikri.fakerapp.data.model.Person
import id.ac.kampusmelayu.amin.zikri.fakerapp.databinding.ItemPersonBinding
import id.ac.kampusmelayu.amin.zikri.fakerapp.util.Constants

class PersonAdapter(
    private val onItemClick: (Person) -> Unit
) : ListAdapter<Person, PersonAdapter.PersonViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val binding = ItemPersonBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PersonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = getItem(position)
        holder.bind(person)
        holder.itemView.setOnClickListener { onItemClick(person) }
    }

    class PersonViewHolder(private val binding: ItemPersonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(person: Person) {
            binding.tvName.text = person.fullName
            binding.tvEmail.text = person.email ?: "-"

            // City + country
            val city = person.address?.city ?: "-"
            val country = person.address?.country ?: ""
            binding.tvCity.text = if (country.isNotEmpty()) "$city, $country" else city

            // Gender chip
            binding.chipGender.text = person.gender ?: "-"
            val context = binding.root.context
            val chipColor = when (person.gender) {
                Constants.GENDER_MALE -> R.color.stat_card_blue
                Constants.GENDER_FEMALE -> R.color.stat_card_pink
                else -> R.color.brand_primary
            }
            binding.chipGender.setChipBackgroundColorResource(chipColor)
            binding.chipGender.setTextColor(
                androidx.core.content.ContextCompat.getColor(context, R.color.white)
            )

            // Avatar via Glide
            Glide.with(binding.root.context)
                .load(person.image)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .into(binding.imgAvatar)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Person>() {
            override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
                return oldItem == newItem
            }
        }
    }
}
