package com.example.saxiinventory.domain.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user_id") val userId: Int,
    val name: String,
    val role: String,
    @SerializedName("contact_info") val contactInfo: String
)