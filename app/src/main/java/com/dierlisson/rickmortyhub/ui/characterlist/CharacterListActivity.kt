package com.dierlisson.rickmortyhub.ui.characterlist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dierlisson.rickmortyhub.data.local.database.AppDatabase
import com.dierlisson.rickmortyhub.data.remote.network.NetworkConfig
import com.dierlisson.rickmortyhub.data.repository.CharacterRepositoryImpl
import com.dierlisson.rickmortyhub.ui.ViewModelFactory
import com.dierlisson.rickmortyhub.ui.characterdetail.CharacterDetailActivity
import com.dierlisson.rickmortyhub.ui.characterlist.adapter.CharacterAdapter
import com.dierlisson.rickmortyhub.ui.characterlist.viewmodel.CharacterListViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.dierlisson.rickmortyhub.databinding.ActivityCharacterListBinding 
import com.dierlisson.rickmortyhub.R

class CharacterListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCharacterListBinding
    private lateinit var viewModel: CharacterListViewModel
    private lateinit var adapter: CharacterAdapter
    
    private var currentSelectedStatus: String? = null
    private var currentSelectedGender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDependencies()
        setupRecyclerView()
        setupSearch()
        setupFilters()
        setupRetryButton()
        observeState()
    }

    private fun setupDependencies() {
        val api = NetworkConfig.provideApi()
        val dao = AppDatabase.getDatabase(this).characterDao()
        val repository = CharacterRepositoryImpl(api, dao)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[CharacterListViewModel::class.java]
    }

    private fun setupRecyclerView() {
        adapter = CharacterAdapter(
            onCharacterClick = { character ->
                val intent = Intent(this, CharacterDetailActivity::class.java)
                intent.putExtra("CHARACTER_ID", character.id)
                startActivity(intent)
            },
            onFavoriteClick = { character ->
                viewModel.toggleFavorite(character)
            }
        )

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        viewModel.loadNextPage()
                    }
                }
            }
        })
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener {
            viewModel.searchCharacters(it.toString())
        }
    }

    private fun setupFilters() {
        val statusMap = mapOf(
            binding.tvFilterAlive to "alive",
            binding.tvFilterDead to "dead",
            binding.tvFilterUnknownStatus to "unknown"
        )
        
        val genderMap = mapOf(
            binding.tvFilterFemale to "female",
            binding.tvFilterMale to "male"
        )

        statusMap.forEach { (view, status) ->
            view.setOnClickListener {
                currentSelectedStatus = if (currentSelectedStatus == status) null else status
                updateFilterUI(statusMap, currentSelectedStatus)
                viewModel.filterBy(currentSelectedStatus, currentSelectedGender)
            }
        }

        genderMap.forEach { (view, gender) ->
            view.setOnClickListener {
                currentSelectedGender = if (currentSelectedGender == gender) null else gender
                updateFilterUI(genderMap, currentSelectedGender)
                viewModel.filterBy(currentSelectedStatus, currentSelectedGender)
            }
        }
    }

    private fun updateFilterUI(map: Map<android.widget.TextView, String>, selectedValue: String?) {
        map.forEach { (view, value) ->
            if (value == selectedValue) {
                view.setBackgroundResource(R.drawable.bg_chip_selected)
                view.setTextColor(android.graphics.Color.WHITE)
            } else {
                view.setBackgroundResource(R.drawable.bg_chip)
                view.setTextColor(getColor(R.color.text_secondary))
            }
        }
    }

    private fun setupRetryButton() {
        binding.btnRetry.setOnClickListener {
            viewModel.searchCharacters(binding.etSearch.text.toString())
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                adapter.submitList(state.characters)

                binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                binding.progressBarBottom.visibility = if (state.isLoadingMore) View.VISIBLE else View.GONE

                binding.tvEmpty.visibility = if (!state.isLoading && state.characters.isEmpty() && state.error == null) View.VISIBLE else View.GONE

                if (state.error != null && state.characters.isEmpty()) {
                    binding.llErrorState.visibility = View.VISIBLE
                    binding.tvError.text = state.error
                } else {
                    binding.llErrorState.visibility = View.GONE
                    if (state.error != null) {
                        Toast.makeText(this@CharacterListActivity, state.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
