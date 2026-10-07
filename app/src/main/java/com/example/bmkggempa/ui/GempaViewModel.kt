package com.example.bmkggempa.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bmkggempa.data.remote.RetrofitClient
import com.example.bmkggempa.data.repository.GempaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GempaViewModel(private val repository: GempaRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<GempaUiState>(GempaUiState.Loading)
    val uiState: StateFlow<GempaUiState> = _uiState.asStateFlow()

    init { loadGempa() }

    fun loadGempa() = viewModelScope.launch {
        _uiState.value = GempaUiState.Loading
        _uiState.value = runCatching { repository.getGempaTerkini() }
            .fold(
                onSuccess = { GempaUiState.Success(it) },
                onFailure = { GempaUiState.Error(it.message ?: "Gagal mengambil data BMKG") }
            )
    }

    companion object {
        fun factory() = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                GempaViewModel(GempaRepository(RetrofitClient.api)) as T
        }
    }
}
