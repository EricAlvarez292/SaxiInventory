package com.example.saxiinventory.domain.model

data class HomeDashBoardModel(
    val type: HomeDashBoardItemType,
    val title: String,
    var quantity: String,
)

enum class HomeDashBoardItemType(val value: String) {
    PRODUCTS("Products"),
    PRODUCT_CATEGORY("Product Category"),
    SUPPLIERS("Suppliers")
}
