package com.joaquin.josuna_inventory.features.auth.data.mapper

import com.google.firebase.auth.FirebaseUser
import com.joaquin.josuna_inventory.features.auth.domain.entities.User

fun FirebaseUser.toDomain(): User = User(
    uid = uid,
    name = displayName ?: "",
    email = email ?: ""
)
