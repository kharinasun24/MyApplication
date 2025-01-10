package com.example.myapplication.database

import android.app.Application

class GroceryApplication : Application() {
    val database: GroceryDatabase by lazy { GroceryDatabase.getDatabase(this) }
}
