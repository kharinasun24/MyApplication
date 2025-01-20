// MainActivity.kt
package com.example.myapplication

import android.net.Uri
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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

    private lateinit var contactPickerLauncher: ActivityResultLauncher<Intent>

    private lateinit var binding: ActivityMainBinding

    private val groceryViewModel: GroceryViewModel by viewModels {
        GroceryViewModelFactory((application as GroceryApplication).database.groceryDao())
    }

    private lateinit var groceryAdapter: GroceryAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        contactPickerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val phoneNumber = data?.getStringExtra("SELECTED_PHONE_NUMBER")
                //Log.d("ContactsActivity", "Selected Phone Number: phoneNumber")
                // Weitere Verarbeitung hier:
        //////////////////////////////////////////////////////////////

                //if (resultCode == RESULT_OK) { TODO: Erst testen. Nach dem Testen RESULT_OK ebenfalls überprüft werden.

                groceryViewModel.items.value?.let { items ->
                    if (items.isNotEmpty() && phoneNumber != null) {

                        val itemsString = items.joinToString(";") { it.toStringRepresentation() }

                        val smsIntent = Intent(Intent.ACTION_SENDTO).apply {
                            setData(Uri.parse("smsto:$phoneNumber"))  // URI für SMS mit Telefonnummer
                            putExtra("sms_body", itemsString)            // Nachrichtentext
                        }

                        if (smsIntent.resolveActivity(packageManager) != null) {
                            val intent = smsIntent?.toString() ?: "smsIntent ist null"
                            //Log.d("###################################################################", intent)
                            startActivity(smsIntent)
                        } else {
                            Toast.makeText(this, getString(R.string.no_sms_app), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.nothing_to_share), Toast.LENGTH_SHORT).show()
                    }
                }

        //////////////////////////////////////////////////////////////
            }
        }


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
                    groceryViewModel.items.value?.let { items ->
                        if (items.isNotEmpty()) {
                            val share = Intent.createChooser(Intent().apply {
                                action = Intent.ACTION_SEND

                                //Das Problem ist eben, dass beim Teilen dem User die gesamte Liste angezeigt wird...
                                val itemsString = items.joinToString(";") { it.toStringRepresentation() }
                                putExtra(Intent.EXTRA_TEXT, itemsString)
                                type = "text/plain"
                                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                            }, null)
                            startActivity(share)
                        } else {

                            Toast.makeText(this, getString(R.string.nothing_to_share), Toast.LENGTH_SHORT).show()
                        }
                    }
                    updateSelectedItemState(item.itemId)
                    true
                }



                R.id.nav_contacts -> {
                    //val intent = Intent(this, ContactsActivity::class.java)
                    //startActivity(intent)
                    val intent = Intent(this, ContactsActivity::class.java)
                    contactPickerLauncher.launch(intent)
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

