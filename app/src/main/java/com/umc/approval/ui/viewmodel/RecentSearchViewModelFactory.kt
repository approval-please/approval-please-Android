package com.umc.approval.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.umc.approval.ui.repository.RecentSearchFragmentRepository

/**
 * ViewModel에 초기 값을 전달하기 위한 Factory
 * */
class RecentSearchViewModelFactory(private val dataRepository: RecentSearchFragmentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RecentSearchViewModel::class.java)) {
            return RecentSearchViewModel(dataRepository) as T
        }
        throw IllegalArgumentException("unknown viewModel class")
    }
}