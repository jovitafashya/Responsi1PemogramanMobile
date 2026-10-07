package com.example.bmkggempa.ui

import com.example.bmkggempa.data.model.Gempa

sealed interface GempaUiState {
    data object Loading : GempaUiState
    data class Success(val items: List<Gempa>) : GempaUiState
    data class Error(val message: String) : GempaUiState
}
