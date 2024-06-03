package com.example.module.life.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchActivityViewModel : ViewModel() {

    private val _searchContent = MutableStateFlow<String?>(null)
    val searchContent get() = _searchContent.asStateFlow()

    fun setSearchContent(content : String){
        viewModelScope.launch {
            _searchContent.emit(content)
        }
    }
}