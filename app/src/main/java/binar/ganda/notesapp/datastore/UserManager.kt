package binar.ganda.notesapp.datastore

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserManager(context: Context) {

    private val dataStore : DataStore<Preferences> = context.createDataStore(name = "USER_PREF")

    companion object {
        val USERNAME = preferencesKey<String>("USER_USERNAME")
        val PASSWORD = preferencesKey<String>("USER_PASSWORD")
    }

    suspend fun saveDataLogin(username : String, password : String) {
        dataStore.edit{
            it[USERNAME] = username
            it[PASSWORD] = password
        }
    }

    val userUsername : Flow<String> = dataStore.data.map {
        it[USERNAME] ?: ""
    }

    val userPassword : Flow<String> = dataStore.data.map {
        it[PASSWORD] ?: ""
    }

    suspend fun clearDataLogin() {
        dataStore.edit {
            it.clear()
        }
    }

}