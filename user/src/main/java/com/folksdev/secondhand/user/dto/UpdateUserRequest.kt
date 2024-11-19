package com.folksdev.secondhand.user.dto

data class UpdateUserRequest(
        val firstName: String,
        val lastName: String,
        val middleName: String
) {
        constructor() : this("", "", "")

}