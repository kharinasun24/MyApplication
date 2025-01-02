package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.GroceryItem

class GroceryViewModel : ViewModel() {
    private val _items = MutableLiveData<List<GroceryItem>>(emptyList())
    val items: LiveData<List<GroceryItem>> get() = _items

    private var nextId = 1

    fun addItem(item: GroceryItem) {
        val updatedList = _items.value.orEmpty().toMutableList()
        updatedList.add(item.copy(id = nextId++))
        _items.value = updatedList
    }

    fun deleteItem(item: GroceryItem) {
        val updatedList = _items.value.orEmpty().filter { it.id != item.id }
        _items.value = updatedList
    }
}
