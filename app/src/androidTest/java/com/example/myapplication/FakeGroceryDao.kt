package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.database.GroceryDao
import com.example.myapplication.model.GroceryItem

class FakeGroceryDao : GroceryDao {

    private val items = mutableListOf<GroceryItem>()

    override fun getAllItems(): LiveData<List<GroceryItem>> {
        return MutableLiveData(items)
    }

    override suspend fun clearAllItems() {
        TODO("Not yet implemented")
    }

    override suspend fun insert(item: GroceryItem) {
        items.add(item)
    }

    override suspend fun delete(item: GroceryItem) {
        items.remove(item)
    }

    fun clearAll() {
        items.clear()
    }
}
