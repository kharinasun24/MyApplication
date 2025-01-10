package com.example.myapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grocery_items")
@Parcelize // Diese Annotation macht die Klasse Parcelable
data class GroceryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val quantity: Int
) : Parcelable // Implementiere Parcelable
{
    fun toStringRepresentation(): String {
        return "$name,$quantity"
    }
}



