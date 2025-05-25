package com.example.saxiinventory.data.repository

import com.example.saxiinventory.common.CustomNetworkError
import com.example.saxiinventory.common.Result
import com.example.saxiinventory.data.remote.product.ProductRemoteDataManager
import com.example.saxiinventory.domain.model.NetworkResponse
import com.example.saxiinventory.domain.model.Product
import com.example.saxiinventory.domain.model.Supplier
import com.example.saxiinventory.domain.model.Transaction
import com.example.saxiinventory.domain.model.User
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productRemoteDataManager: ProductRemoteDataManager) {

    fun getFlowProducts(): Flow<Result<List<Product>?, CustomNetworkError>> {
        return productRemoteDataManager.getFlowProducts()
    }

    fun getFlowSuppliers(): Flow<Result<List<Supplier>?, CustomNetworkError>> {
        return productRemoteDataManager.getFlowSuppliers()
    }

    fun getFlowExternalProducts(supplierId: Int): Flow<Result<List<Product>?, CustomNetworkError>> {
        return productRemoteDataManager.getFlowExternalProducts(supplierId)
    }

    fun createPurchase(transaction: Transaction): Flow<Result<NetworkResponse?, CustomNetworkError>> {
        return productRemoteDataManager.createPurchase(transaction)
    }

    fun getFlowUsers(): Flow<Result<List<User>?, CustomNetworkError>> {
        return productRemoteDataManager.getFlowUsers()
    }

}