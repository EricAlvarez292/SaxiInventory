package com.example.saxiinventory.data.remote.product

import com.example.saxiinventory.common.CustomNetworkError
import com.example.saxiinventory.common.Result
import com.example.saxiinventory.common.toCustomNetworkError
import com.example.saxiinventory.data.remote.ApiService
import com.example.saxiinventory.domain.model.NetworkResponse
import com.example.saxiinventory.domain.model.Product
import com.example.saxiinventory.domain.model.Supplier
import com.example.saxiinventory.domain.model.Transaction
import com.example.saxiinventory.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class ProductRemoteDataManagerImpl(private val api: ApiService) : ProductRemoteDataManager {

    override fun getFlowProducts(): Flow<Result<List<Product>?, CustomNetworkError>> =
        flow {
            emit(Result.Loading)
            val response = api.getProducts()
            if (response.isSuccessful) {
                emit(Result.Success(response.body()))
            } else {
                Timber.tag("EricTest").d("Error: ${response.message()}")
                Timber.tag("EricTest").d("Error: ${response.errorBody()}")
                emit(Result.Error(CustomNetworkError.UNKNOWN, response.message()))
            }
        }.catch {
            Timber.tag("EricTest").d("Error: $it")
            emit(Result.Error(it.toCustomNetworkError()))
        }

    override fun getFlowSuppliers(): Flow<Result<List<Supplier>?, CustomNetworkError>> =
        flow {
            emit(Result.Loading)
            val response = api.getSuppliers()
            if (response.isSuccessful) {
                emit(Result.Success(response.body()))
            } else {
                Timber.tag("EricTest").d("Error: ${response.message()}")
                Timber.tag("EricTest").d("Error: ${response.errorBody()}")
                emit(Result.Error(CustomNetworkError.UNKNOWN, response.message()))
            }
        }.catch {
            Timber.tag("EricTest").d("Error: $it")
            emit(Result.Error(it.toCustomNetworkError()))
        }

    override fun getFlowExternalProducts(supplierId: Int): Flow<Result<List<Product>?, CustomNetworkError>> =
        flow {
            emit(Result.Loading)
            val response = api.getExternalProducts(supplierId)
            if (response.isSuccessful) {
                emit(Result.Success(response.body()))
            } else {
                Timber.tag("EricTest").d("Error: ${response.message()}")
                Timber.tag("EricTest").d("Error: ${response.errorBody()}")
                emit(Result.Error(CustomNetworkError.UNKNOWN, response.message()))
            }
        }.catch {
            Timber.tag("EricTest").d("Error: $it")
            emit(Result.Error(it.toCustomNetworkError()))
        }

    override fun createPurchase(transaction: Transaction): Flow<Result<NetworkResponse?, CustomNetworkError>> =
        flow {
            emit(Result.Loading)
            val response = api.createPurchase(transaction)
            if (response.isSuccessful) {
                emit(Result.Success(response.body()))
            } else {
                Timber.tag("EricTest").d("Error: ${response.message()}")
                Timber.tag("EricTest").d("Error: ${response.errorBody()}")
                emit(Result.Error(CustomNetworkError.UNKNOWN, response.message()))
            }
        }.catch {
            Timber.tag("EricTest").d("Error: $it")
            emit(Result.Error(it.toCustomNetworkError()))
        }

    override fun getFlowUsers(): Flow<Result<List<User>?, CustomNetworkError>> =
        flow {
            emit(Result.Loading)
            val response = api.getUsers()
            if (response.isSuccessful) {
                emit(Result.Success(response.body()))
            } else {
                Timber.tag("EricTest").d("Error: ${response.message()}")
                Timber.tag("EricTest").d("Error: ${response.errorBody()}")
                emit(Result.Error(CustomNetworkError.UNKNOWN, response.message()))
            }
        }.catch {
            Timber.tag("EricTest").d("Error: $it")
            emit(Result.Error(it.toCustomNetworkError()))
        }

}