package com.umc.approval.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.umc.approval.databinding.ActivitySearchBinding
import com.umc.approval.data.db.database.RecentSearchDatabase
import com.umc.approval.data.repository.search.SearchFragmentRepository
import com.umc.approval.ui.viewmodel.search.RecentSearchViewModel
import com.umc.approval.ui.viewmodel.search.RecentSearchViewModelFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    lateinit var viewModel: RecentSearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val database = RecentSearchDatabase.getInstance(this)
        val dataRepository = SearchFragmentRepository(database)
        val factory = RecentSearchViewModelFactory(dataRepository)
        viewModel = ViewModelProvider(this, factory).get(RecentSearchViewModel::class.java)
    }
}