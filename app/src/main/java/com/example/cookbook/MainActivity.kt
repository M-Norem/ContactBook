package com.example.cookbook

import ContactDialog
import ContactRow
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : ComponentActivity() {

    private val sharedPreferencesName = "ContactBook"
    private val sharedPreferencesKey = "contacts"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var showDialog by remember { mutableStateOf(false) }
            var selectedContact by remember { mutableStateOf<Contact?>(null) }
            val contacts = remember { mutableStateListOf<Contact>() }

            // Load contacts from SharedPreferences
            LaunchedEffect(Unit) {
                contacts.clear()
                contacts.addAll(loadContacts())
            }

            MaterialTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Button(onClick = { showDialog = true }) {
                        Text("Add Contact")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyColumn {
                        items(contacts.size) { index ->
                            val contact = contacts[index]
                            ContactRow(
                                contact = contact,
                                onEdit = {
                                    selectedContact = contact
                                    showDialog = true
                                },
                                onDelete = {
                                    contacts.removeAt(index)
                                    saveContacts(contacts)
                                }
                            )
                        }
                    }

                    if (showDialog) {
                        ContactDialog(
                            initialName = selectedContact?.name ?: "",
                            initialPhone = selectedContact?.phone ?: "",
                            onDismiss = {
                                showDialog = false
                                selectedContact = null
                            },
                            onSave = { name, phone ->
                                if (selectedContact == null) {
                                    // Add a new contact
                                    contacts.add(Contact(name, phone))
                                } else {
                                    // Update the existing contact
                                    val index = contacts.indexOf(selectedContact)
                                    if (index != -1) {
                                        contacts[index] = Contact(name, phone)
                                    }
                                }
                                saveContacts(contacts)
                                showDialog = false
                                selectedContact = null
                            }
                        )
                    }
                }
            }
        }
    }

    private fun loadContacts(): List<Contact> {
        val sharedPreferences = getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(sharedPreferencesKey, null)
        return if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<List<Contact>>() {}.type
            Gson().fromJson(json, type)
        } else {
            emptyList()
        }
    }

    private fun saveContacts(contacts: List<Contact>) {
        val sharedPreferences = getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(contacts)
        editor.putString(sharedPreferencesKey, json)
        editor.apply()
    }

    data class Contact(var name: String, var phone: String)
}
