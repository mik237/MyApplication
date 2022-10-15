package com.example.myapplication.datastore

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.Address
import com.example.myapplication.Person
import com.example.myapplication.databinding.ActivityDatastoreBinding
import com.example.myapplication.models.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

@AndroidEntryPoint
class DatastoreActivity : AppCompatActivity() {


    lateinit var binding: ActivityDatastoreBinding

//    lateinit var dataStoreManager: DataStoreManager

    lateinit var viewModel: DatastoreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDatastoreBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel = ViewModelProvider(this).get(DatastoreViewModel::class.java)

//        dataStoreManager = DataStoreManager(this@DatastoreActivity)

        binding.btnSave.setOnClickListener {
            /*val phoneBook = PhoneBook(
                binding.etName.text.toString(),
                binding.etAge.text.toString().toInt(),
                binding.etAddress.text.toString(),
                binding.etPhone.text.toString()
            )
            viewModel.saveDataToDatastore(phoneBook)*/

            viewModel.name.value = "Ibrahim"
            viewModel.age.value = 31
        }

        binding.btnSaveProto.setOnClickListener {
            val age = binding.etAge.text?.toString()?.toInt() ?: 0
//            val address = binding.etAddress.text.toString()
            val name = binding.etName.text.toString()

            val address = com.example.myapplication.models.Address(
                flatNumber = 304,
                street = "Burjuman",
                city = "Dubai",
                country = "United Arab Emirates"
            )
            val user = User(name = name, address = address, age = age)
            viewModel.updateUser(user)

            /*val address = Address.newBuilder()
                .setCity("Dubai")
                .setStreet("Burjuman")
                .setCountry("UAE")
                .setFlatNumber(304)
                .build()*/

            /*val person=Person.newBuilder()
                .setAge(age)
                .setName(name)
                .setAddress(address)
                .build()
            viewModel.updatePerson(person)*/
        }

        listenForData()



        viewModel.isAuthorized.observe(this){
            Log.d("_TAG", "Authrized: $it")
        }
    }


    private fun listenForData() {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.listenToDatastore().catch { e ->
                e.printStackTrace()
            }.collect {
                withContext(Dispatchers.Main) {
                    binding.tvName.text = it.name
                    binding.tvAddress.text = it.address
                    binding.tvAge.text = "${it.age}"
                    binding.tvPhone.text = it.phoneNumber
                }
            }

        }

        /*lifecycleScope.launch {
            viewModel.readPersion().catch { e ->
                Log.d("Error", "listenForData: ${e.message.toString()}")
            }
                .collect { person ->
                    binding.tvPersonName.text = person.name
                    binding.tvPersonAddress.text = "${person.address.flatNumber} ${person.address.street} ${person.address.city} ${person.address.country}"
                    binding.tvPersonAge.text = "${person.age}"
                }
        }*/

        lifecycleScope.launch {
            viewModel.readUser().catch { e ->
                Log.d("Error", "listenForData: ${e.message.toString()}")
            }
                .collect { user ->
                    binding.tvPersonName.text = user.name
                    binding.tvPersonAddress.text = "${user.address?.flatNumber} ${user.address?.street} ${user.address?.city} ${user.address?.country}"
                    binding.tvPersonAge.text = "${user.age}"
                }
        }

        lifecycleScope.launch{

        }





    }


}