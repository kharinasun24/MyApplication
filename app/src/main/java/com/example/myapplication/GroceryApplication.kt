package com.example.myapplication

import android.app.Application

class GroceryApplication : Application() {
    val database: GroceryDatabase by lazy { GroceryDatabase.getDatabase(this) }
}
