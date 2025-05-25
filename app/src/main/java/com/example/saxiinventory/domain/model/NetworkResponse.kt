package com.example.saxiinventory.domain.model

import com.google.gson.annotations.SerializedName

data class NetworkResponse(
    val message: String,
    @SerializedName("transaction_id")
    val transactionId: Int,
)
