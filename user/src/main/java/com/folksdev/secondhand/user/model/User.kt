package com.folksdev.secondhand.user.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
data class User2(
        @field:Id
        @field:GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        val mail: String,
        val firstName: String,
        val lastName: String,
        val middleName: String
){
        constructor() : this(null, "", "", "", "") // Parametresiz yapıcı
}