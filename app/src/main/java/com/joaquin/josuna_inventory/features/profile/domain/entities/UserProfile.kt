package com.joaquin.josuna_inventory.features.profile.domain.entities

data class UserProfile(
    val uid: String,
    val name: String,
    val email: String,
    val photoUrl: String = "",
    val createdAt: Long = 0L
)

