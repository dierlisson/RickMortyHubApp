package com.dierlisson.rickmortyhub.ui.characterdetail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import com.dierlisson.rickmortyhub.data.local.database.AppDatabase
import com.dierlisson.rickmortyhub.data.remote.network.NetworkConfig
import com.dierlisson.rickmortyhub.data.repository.CharacterRepositoryImpl
import com.dierlisson.rickmortyhub.databinding.ActivityCharacterDetailBinding
import com.dierlisson.rickmortyhub.ui.ViewModelFactory
import com.dierlisson.rickmortyhub.ui.characterdetail.viewmodel.CharacterDetailViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.dierlisson.rickmortyhub.R

class CharacterDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCharacterDetailBinding
    private lateinit var viewModel: CharacterDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDependencies()
        
        val characterId = intent.getIntExtra("CHARACTER_ID", -1)
        if (characterId != -1) {
            viewModel.loadCharacter(characterId)
        }

        setupToolbar()
        observeState()
    }

    private fun setupDependencies() {
        val repository = CharacterRepositoryImpl(NetworkConfig.provideApi(), AppDatabase.getDatabase(this).characterDao())
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[CharacterDetailViewModel::class.java]
    }

    private fun setupToolbar() {
        binding.ivBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        
        binding.ivFavoriteTop.setOnClickListener {
            viewModel.toggleFavorite()
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                
                state.character?.let { character ->
                    binding.tvTopTitle.text = character.name
                    binding.tvName.text = character.name
                    
                    binding.tvChipStatus.text = character.status
                    binding.tvChipSpecies.text = character.species
                    binding.tvChipGender.text = character.gender
                    
                    val dotRes = when (character.status.lowercase()) {
                        "alive" -> R.drawable.bg_status_dot_alive
                        "dead" -> R.drawable.bg_status_dot_dead
                        else -> R.drawable.bg_status_dot_unknown
                    }
                    binding.tvChipStatus.setBackgroundResource(dotRes)
                    
                    binding.tvStatus.text = character.status
                    binding.tvSpecies.text = character.species
                    binding.tvGender.text = character.gender

                    binding.ivLargeImage.load(character.imageUrl) {
                        crossfade(true)
                    }

                    val favIcon = if (character.isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_outline
                    binding.ivFavoriteTop.setImageResource(favIcon)
                    if (character.isFavorite) {
                        binding.ivFavoriteTop.setColorFilter(android.graphics.Color.RED)
                    } else {
                        binding.ivFavoriteTop.clearColorFilter()
                    }
                }

                state.error?.let {
                    // Tratar visualmente o erro
                }
            }
        }
    }
}
