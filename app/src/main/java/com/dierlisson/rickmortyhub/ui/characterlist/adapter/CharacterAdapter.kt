package com.dierlisson.rickmortyhub.ui.characterlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dierlisson.rickmortyhub.domain.model.Character
import com.dierlisson.rickmortyhub.R

class CharacterAdapter(
    private val onCharacterClick: (Character) -> Unit,
    private val onFavoriteClick: (Character) -> Unit
) : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    private var characters: List<Character> = emptyList()

    fun submitList(newList: List<Character>) {
        characters = newList
        notifyDataSetChanged() 
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        // Exemplo simplificado. Assumindo que criaremos o layout depois.
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_character, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(characters[position])
    }

    override fun getItemCount() = characters.size

    inner class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivImage: ImageView = itemView.findViewById(R.id.ivCharacterImage)
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvStatusSpecies: TextView = itemView.findViewById(R.id.tvStatusSpecies)
        private val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        private val vStatusDot: View = itemView.findViewById(R.id.vStatusDot)
        private val ivFavorite: ImageView = itemView.findViewById(R.id.ivFavorite)

        fun bind(character: Character) {
            tvName.text = character.name
            tvStatusSpecies.text = "${character.status} • ${character.species}"
            tvLocation.text = character.locationName ?: "Unknown Location"

            val dotRes = when (character.status.lowercase()) {
                "alive" -> R.drawable.bg_status_dot_alive
                "dead" -> R.drawable.bg_status_dot_dead
                else -> R.drawable.bg_status_dot_unknown
            }
            vStatusDot.setBackgroundResource(dotRes)

            ivImage.load(character.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_placeholder)
                error(R.drawable.ic_error)
            }

            val favIcon = if (character.isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_outline
            ivFavorite.setImageResource(favIcon)
            if (character.isFavorite) {
                ivFavorite.setColorFilter(android.graphics.Color.RED)
            } else {
                ivFavorite.clearColorFilter()
            }

            itemView.setOnClickListener { onCharacterClick(character) }
            ivFavorite.setOnClickListener { onFavoriteClick(character) }
        }
    }
}
