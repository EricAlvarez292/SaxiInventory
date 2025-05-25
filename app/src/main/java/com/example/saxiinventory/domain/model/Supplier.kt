package com.example.saxiinventory.domain.model

import com.google.gson.annotations.SerializedName

data class Supplier(
    @SerializedName("supplier_id")
    val supplierId: Int,
    val name: String,
    @SerializedName("contact_info")
    val contactInfo: String,
    val address: String
)
