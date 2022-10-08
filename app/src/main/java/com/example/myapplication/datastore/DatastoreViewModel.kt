package com.example.myapplication.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.*
import com.example.myapplication.Person
import com.example.myapplication.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class DatastoreViewModel @Inject constructor(
    val dataStore: DataStore<Preferences>,
    val personDatastore: DataStore<Person>,
    val userDataStore: DataStore<User>
) : ViewModel() {

    init {
        Log.d("_TAG_", ": datastore initialized? : ${dataStore.toString()}")
    }

    companion object {
        val NAME = stringPreferencesKey("NAME")
        val PHONE_NUMBER = stringPreferencesKey("PHONE_NUMBER")
        val AGE = intPreferencesKey("AGE")
        val ADDRESS = stringPreferencesKey("ADDRESS")

    }

    fun saveDataToDatastore(phonebook: PhoneBook) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.edit { userDataStore ->
                userDataStore[DataStoreManager.NAME] = phonebook.name
                userDataStore[DataStoreManager.PHONE_NUMBER] = phonebook.phoneNumber
                userDataStore[DataStoreManager.AGE] = phonebook.age
                userDataStore[DataStoreManager.ADDRESS] = phonebook.address
            }
        }
    }

    fun listenToDatastore() = dataStore.data.map {
        PhoneBook(
            it[NAME] ?: "",
            it[AGE] ?: 0,
            it[ADDRESS] ?: "",
            it[PHONE_NUMBER] ?: ""
        )
    }


    fun updatePersonName(name: String) {
        viewModelScope.launch {
            personDatastore.updateData { person ->
                person.toBuilder()
                    .setName(name)
                    .build()
            }
        }
    }

    val name = MutableLiveData<String>("")
    val age = MutableLiveData<Int>(0)

    val isAuthorized: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(name) { name ->
            value =
                (name == "Ibrahim" && age.value != null && age.value!! >= 30)
        }
        addSource(age) { age ->
            value =
                (age >= 30 && name.value == "Ibrahim")
        }
    }

    fun readPersion(): Flow<Person> = personDatastore.data
        .catch { e ->
            if (e is IOException) {
                Log.d("Error", e.message.toString())
                emit(Person.getDefaultInstance())
            } else throw e
        }

    fun updatePerson(p: Person) {
        viewModelScope.launch {
            personDatastore.updateData { person ->
                person.toBuilder()
                    .setName(p.name)
                    .setAddress(p.address)
                    .setAge(p.age)
                    .build()
            }
        }
    }

    fun updateUser(u: User) {
        viewModelScope.launch {
            userDataStore.updateData { user ->
                user.copy(name = u.name, age = u.age, address = u.address)
            }
        }
    }

    fun readUser(): Flow<User> = userDataStore.data
        .catch { e ->
            if (e is IOException) {
                Log.d("Error", e.message.toString())
                emit(User())
            } else throw e
        }
}