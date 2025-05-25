package com.example.saxiinventory.domain.model

import com.google.gson.annotations.SerializedName

data class Transaction(
    @SerializedName("transaction_type") val transactionType: String,
    @SerializedName("user_id") val userId: Int,
    val product: TransactionProduct,
    @SerializedName("transaction_details") val transactionDetails: TransactionDetails
)


data class TransactionDetails(
    @SerializedName("supplier_id") val supplierId: Int?,
    val name: String?,
    val quantity: Int,
    @SerializedName("total_amount") val totalAmount: Double,
    @SerializedName("payment_method") val paymentMethod: String,
    @SerializedName("expected_delivery_date") val expectedDeliveryDate: String?,

    /*Sell*/
    @SerializedName("customer_id") val customerId: Int?,
    @SerializedName("customer_name") val customerName: String?
)