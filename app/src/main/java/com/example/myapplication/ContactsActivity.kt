package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.adapter.ContactAdapter

class ContactsActivity : AppCompatActivity() {

    private val contactList = mutableListOf<Contact>()

    private lateinit var listViewContacts: ListView
    private val REQUEST_CONTACTS_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        listViewContacts = findViewById(R.id.listViewContacts)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_CONTACTS_PERMISSION
            )
        } else {
            loadContacts()
        }


        listViewContacts.setOnItemClickListener { _, _, position, _ ->

            val selectedContact = contactList[position]
            val resultIntent = Intent().apply {
                putExtra("selected_contact", selectedContact)
            }
            setResult(RESULT_OK, resultIntent)
            finish()

        }
    }

    private fun loadContacts() {

        val cursor: Cursor? = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        )

        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val phoneIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (it.moveToNext()) {
                val name = it.getString(nameIndex)
                val phoneNumber = it.getString(phoneIndex)
                contactList.add(Contact(name, phoneNumber))
            }
        }

        val listViewContacts = findViewById<ListView>(R.id.listViewContacts)
        val adapter = ContactAdapter(this, contactList)
        listViewContacts.adapter = adapter

        listViewContacts.setOnItemClickListener { _, _, position, _ ->
            val selectedContact = contactList[position]
            val resultIntent = Intent().apply {
                putExtra("SELECTED_PHONE_NUMBER", selectedContact.phoneNumber)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CONTACTS_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadContacts()
        }
    }
}