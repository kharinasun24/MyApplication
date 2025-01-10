package com.example.myapplication.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myapplication.model.GroceryItem

@Dao
interface GroceryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: GroceryItem)

    @Delete
    suspend fun delete(item: GroceryItem)

    @Query("SELECT * FROM grocery_items ORDER BY id ASC")
    fun getAllItems(): LiveData<List<GroceryItem>>

    @Query("DELETE FROM grocery_items")
    suspend fun clearAllItems()
}
