// MainActivity.kt
package com.example.myapplication

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.GroceryItem
import com.example.myapplication.viewmodel.GroceryViewModel
import com.example.myapplication.adapter.GroceryAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    //private val groceryViewModel: GroceryViewModel by viewModels()

    private val groceryViewModel: GroceryViewModel by viewModels {
        GroceryViewModelFactory((application as GroceryApplication).database.groceryDao())
    }

    private lateinit var groceryAdapter: GroceryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        groceryAdapter = GroceryAdapter(
            onDelete = { groceryViewModel.deleteItem(it) }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = groceryAdapter
        }

        binding.buttonAdd.setOnClickListener {
            val itemName = binding.editTextItemName.text.toString()
            val quantity = binding.editTextQuantity.text.toString().toIntOrNull() ?: 0

            if (itemName.isNotBlank()) {
                val item = GroceryItem(name = itemName, quantity = quantity)
                groceryViewModel.addItem(item)
                binding.editTextItemName.text.clear()
                binding.editTextQuantity.text.clear()
            }
        }


        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {

            //TODO: Implement Contact Activity
                /*
                R.id.nav_contacts -> {
                    val intent = Intent(this, ContactsActivity::class.java)
                    startActivity(intent)
                    true
                }
                */
                R.id.nav_delete_all -> {
                    groceryViewModel.clearAllItems()
                    true
                }

                else -> false
            }
        }

        groceryViewModel.items.observe(this, Observer {
            groceryAdapter.submitList(it)
        })
    }
}
