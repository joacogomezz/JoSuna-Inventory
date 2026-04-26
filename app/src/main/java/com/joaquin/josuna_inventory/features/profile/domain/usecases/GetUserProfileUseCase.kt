package com.joaquin.josuna_inventory.features.profile.domain.usecases

import com.joaquin.josuna_inventory.features.profile.domain.entities.UserProfile
import com.joaquin.josuna_inventory.features.profile.domain.repositories.ProfileRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    operator fun invoke(): UserProfile? = repository.getUserProfile()
}

