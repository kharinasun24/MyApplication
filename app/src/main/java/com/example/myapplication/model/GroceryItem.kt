package com.example.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
data class GroceryItem(
    val id: Int = 0,
    val name: String,
    val quantity: Int
)
*/



@Entity(tableName = "grocery_items")
data class GroceryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val quantity: Int
)



