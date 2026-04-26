package com.joaquin.josuna_inventory.core.preferences
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class ProfilePreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        val PROFILE_PHOTO_URI_KEY = stringPreferencesKey("profile_photo_uri")
    }
    val profilePhotoUri: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PROFILE_PHOTO_URI_KEY]
    }
    suspend fun setProfilePhotoUri(uriString: String) {
        context.dataStore.edit { preferences ->
            preferences[PROFILE_PHOTO_URI_KEY] = uriString
        }
    }
}
