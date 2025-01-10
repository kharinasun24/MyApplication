// MainActivity.kt
package com.example.myapplication

import android.net.Uri
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.GroceryItem
import com.example.myapplication.viewmodel.GroceryViewModel
import com.example.myapplication.adapter.GroceryAdapter
import com.example.myapplication.database.GroceryApplication
import com.example.myapplication.viewmodel.GroceryViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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

                R.id.nav_home -> {
                    //TODO Hier einen createChooser, dass ich es an alle senden kann.
                    groceryViewModel.items.value?.let { items ->
                        val share = Intent.createChooser(Intent().apply {
                            action = Intent.ACTION_SEND

                            val itemsString = items.joinToString(";") { it.toStringRepresentation() }
                            putExtra(Intent.EXTRA_TEXT, itemsString)
                            type = "text/plain"
                            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                        }, null)
                        startActivity(share)
                    }
                    updateSelectedItemState(item.itemId)
                    true
                }



                R.id.nav_contacts -> {
                    val intent = Intent(this, ContactsActivity::class.java)
                    startActivity(intent)
                    updateSelectedItemState(item.itemId)
                    true
                }

                R.id.nav_delete_all -> {
                    groceryViewModel.clearAllItems()
                    updateSelectedItemState(item.itemId)
                    true
                }

                else -> false
            }
        }

        


        groceryViewModel.items.observe(this, Observer {
            groceryAdapter.submitList(it)
        })
    }

    private fun updateSelectedItemState(selectedItemId: Int) {
        for (i in 0 until binding.bottomNavigationView.menu.size()) {
            val menuItem = binding.bottomNavigationView.menu.getItem(i)
            if (menuItem.itemId == selectedItemId) {
                menuItem.isChecked = true // Der gerade ausgewählte Button bleibt ausgewählt
            } else {
                menuItem.isChecked = false // Alle anderen werden deselektiert
            }
        }

    }
}
