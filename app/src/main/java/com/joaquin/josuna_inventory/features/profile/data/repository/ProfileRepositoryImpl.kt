package com.joaquin.josuna_inventory.features.profile.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.joaquin.josuna_inventory.features.profile.domain.entities.UserProfile
import com.joaquin.josuna_inventory.features.profile.domain.repositories.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : ProfileRepository {

    override fun getUserProfile(): UserProfile? {
        val firebaseUser = auth.currentUser ?: return null
        return UserProfile(
            uid = firebaseUser.uid,
            name = firebaseUser.displayName ?: "Usuario",
            email = firebaseUser.email ?: "",
            photoUrl = firebaseUser.photoUrl?.toString() ?: "",
            createdAt = firebaseUser.metadata?.creationTimestamp ?: 0L
        )
    }
}

