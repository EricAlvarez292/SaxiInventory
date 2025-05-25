package com.example.saxiinventory.domain.model

import com.google.gson.annotations.SerializedName


data class Product(
    @SerializedName("product_id")
    val productId: Int,
    val name: String,
    val description: String,
    val category: String,
    val price: Double,
    @SerializedName("supplier_id")
    val supplierId: Int,
    @SerializedName("quantity_available")
    val quantityAvailable: Int
) {
    fun toTransactionProduct() = TransactionProduct(
        productId = productId,
        name = name,
        description = description,
        category = category,
        price = price,
        supplierId = supplierId
    )
}

data class TransactionProduct(
    @SerializedName("product_id")
    val productId: Int,
    val name: String,
    val description: String,
    val category: String,
    val price: Double,
    @SerializedName("supplier_id")
    val supplierId: Int
)