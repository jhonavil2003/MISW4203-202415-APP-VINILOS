package com.example.app_vinilos_g17.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_vinilos_g17.R
import com.example.app_vinilos_g17.models.Performer
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

class PerformerAdapter : RecyclerView.Adapter<PerformerAdapter.PerformerViewHolder>() {

    private var performers: List<Performer> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun updatePerformers(newPerformers: List<Performer>) {
        performers = newPerformers
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerformerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_performer, parent, false)
        return PerformerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PerformerViewHolder, position: Int) {
        holder.bind(performers[position])
    }

    override fun getItemCount(): Int = performers.size

    class PerformerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewName: TextView = itemView.findViewById(R.id.textViewPerformerName)
        private val textViewBirthDate: TextView = itemView.findViewById(R.id.textViewPerformerBirthDate)
        private val textViewDescription: TextView = itemView.findViewById(R.id.textViewPerformerDescription)
        private val textViewCreationDate: TextView = itemView.findViewById(R.id.textViewPerformerCreationDate)
        private val imageView: ImageView = itemView.findViewById(R.id.imageViewPerformer)

        fun bind(performer: Performer) {
            textViewName.text = performer.name
            textViewBirthDate.text = formatDate(performer.birthDate)
            textViewCreationDate.text = formatDate(performer.creationDate)
            textViewDescription.text = performer.description
            Glide.with(itemView.context)
                .load(performer.image)
                .into(imageView)
        }

        private fun formatDate(dateString: String?): String {

            if (dateString.isNullOrEmpty() || dateString == "No Date Available") {
                return "Fecha no disponible"
            }

            val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val desiredFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            return try {
                val date: Date = originalFormat.parse(dateString) ?: return "Fecha no disponible"
                desiredFormat.format(date)
            } catch (e: Exception) {
                return "Fecha no disponible"
            }
        }

    }
}
