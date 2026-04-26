package com.joaquin.josuna_inventory.features.profile.domain.repositories

import com.joaquin.josuna_inventory.features.profile.domain.entities.UserProfile

interface ProfileRepository {
    fun getUserProfile(): UserProfile?
}

