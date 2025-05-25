package com.example.saxiinventory.data.remote.product

import com.example.saxiinventory.common.CustomNetworkError
import com.example.saxiinventory.common.Result
import com.example.saxiinventory.domain.model.NetworkResponse
import com.example.saxiinventory.domain.model.Product
import com.example.saxiinventory.domain.model.Supplier
import com.example.saxiinventory.domain.model.Transaction
import com.example.saxiinventory.domain.model.User
import kotlinx.coroutines.flow.Flow

interface ProductRemoteDataManager {

    fun getFlowProducts(): Flow<Result<List<Product>?, CustomNetworkError>>

    fun getFlowSuppliers(): Flow<Result<List<Supplier>?, CustomNetworkError>>

    fun getFlowExternalProducts(supplierId: Int): Flow<Result<List<Product>?, CustomNetworkError>>

    fun createPurchase(transaction: Transaction): Flow<Result<NetworkResponse?, CustomNetworkError>>

    fun getFlowUsers(): Flow<Result<List<User>?, CustomNetworkError>>

}