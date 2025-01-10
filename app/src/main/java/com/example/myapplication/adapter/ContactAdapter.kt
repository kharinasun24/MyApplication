package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.myapplication.Contact
import com.example.myapplication.R

class ContactAdapter(context: Context, private val contacts: List<Contact>) :
    ArrayAdapter<Contact>(context, 0, contacts) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.contact_list_item, parent, false)
        val contact = getItem(position)

        val nameView = view.findViewById<TextView>(R.id.textContactName)
        val phoneView = view.findViewById<TextView>(R.id.textContactPhone)

        nameView.text = contact?.name
        phoneView.text = contact?.phoneNumber

        return view
    }
}
