package com.example.myapplication.di

import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.myapplication.Person
import com.example.myapplication.datastore.PersonSerializer
import com.example.myapplication.models.User
import com.example.myapplication.models.UserSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DataStoreModule {

    private const val USER_PREFERENCES = "user_preferences"
    private const val DATA_STORE_FILE_NAME = "person.pb"
    private const val DATA_STORE_USER_FILE_NAME = "user.json"
    private const val DATA_STORE_USER_FILE_NAME_NEW = "user_new.json"


    @Provides
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Singleton
    @Provides
    fun providePreferencesDatastore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(context, USER_PREFERENCES)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(USER_PREFERENCES) }
        )
    }

    @Singleton
    @Provides
    fun provideUserDatastore(@ApplicationContext context: Context):DataStore<Person>{
        return DataStoreFactory.create(
            produceFile = { context.dataStoreFile(DATA_STORE_FILE_NAME)},
            serializer = PersonSerializer
        )
    }

    val Context.oldUserStore by dataStore(fileName = DATA_STORE_USER_FILE_NAME, serializer = UserSerializer)

    @Singleton
    @Provides
    fun provideUserSerializationDatastore(@ApplicationContext context: Context): DataStore<User>{


        return DataStoreFactory.create(
            produceFile = {context.dataStoreFile(DATA_STORE_USER_FILE_NAME_NEW)},
            serializer = UserSerializer,
            migrations = listOf(object : DataMigration<User>{
                override suspend fun cleanUp() {

                }

                override suspend fun migrate(currentData: User): User {
                    val oldUser = context.oldUserStore.data.first()
                   return currentData.copy(name = oldUser.name, age = oldUser.age)
                }

                override suspend fun shouldMigrate(currentData: User): Boolean =true
            })
        )
    }

   /* @Singleton
    @Provides
    fun provideProtoDataStore(@ApplicationContext context: Context): DataStore<UserPreferences> {
        return DataStoreFactory.create(
            serializer = UserPreferencesSerializer,
            produceFile = { context.dataStoreFile(DATA_STORE_FILE_NAME) },
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        )
    }

    @Singleton
    @Provides
    fun provideUserProtoDataStore(@ApplicationContext context: Context): DataStore<User> {
        return DataStoreFactory.create(
            produceFile = { context.dataStoreFile(DATA_STORE_USER_FILE_NAME) },
            serializer = UserSerializer,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }*/
}