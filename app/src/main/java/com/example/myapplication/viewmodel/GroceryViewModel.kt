package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.myapplication.database.GroceryDao
import com.example.myapplication.model.GroceryItem

class GroceryViewModel(private val dao: GroceryDao) : ViewModel() {
    val items = dao.getAllItems()

    fun addItem(item: GroceryItem) {
        viewModelScope.launch {
            dao.insert(item)
        }
    }

    fun deleteItem(item: GroceryItem) {
        viewModelScope.launch {
            dao.delete(item)
        }
    }

    fun clearAllItems() {
        viewModelScope.launch {
            dao.clearAllItems()
        }
    }

}
