package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.GroceryDao
import com.example.myapplication.viewmodel.GroceryViewModel

class GroceryViewModelFactory(private val dao: GroceryDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroceryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GroceryViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}