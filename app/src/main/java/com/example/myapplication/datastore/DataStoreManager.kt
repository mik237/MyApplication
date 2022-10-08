package com.example.myapplication.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreManager @Inject constructor(private val context: Context) {

    private val USER_DATA_STORE = "_userDatastore"
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_DATA_STORE)




    companion object {
        val NAME = stringPreferencesKey("NAME")
        val PHONE_NUMBER = stringPreferencesKey("PHONE_NUMBER")
        val AGE = intPreferencesKey("AGE")
        val ADDRESS = stringPreferencesKey("ADDRESS")

    }

    suspend fun saveToDatastore(phonebook: PhoneBook) {

        context.dataStore.edit { userDataStore ->
            userDataStore[NAME] = phonebook.name
            userDataStore[PHONE_NUMBER] = phonebook.phoneNumber
            userDataStore[AGE] = phonebook.age
            userDataStore[ADDRESS] = phonebook.address
        }
    }

    fun getFromDataStore() = context.dataStore.data.map {
        it.toPreferences()
    }


    ////////////////////////

}