package com.example.saxiinventory.data.remote

import com.example.saxiinventory.common.Constants.EXTERNAL_PRODUCTS_URL
import com.example.saxiinventory.common.Constants.EXTERNAL_SUPPLIERS_URL
import com.example.saxiinventory.common.Constants.PRODUCTS_URL
import com.example.saxiinventory.common.Constants.TRANSACTIONS_URL
import com.example.saxiinventory.common.Constants.USERS_URL
import com.example.saxiinventory.domain.model.NetworkResponse
import com.example.saxiinventory.domain.model.Product
import com.example.saxiinventory.domain.model.Supplier
import com.example.saxiinventory.domain.model.Transaction
import com.example.saxiinventory.domain.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @Headers("Content-Type: application/json")
    @GET(PRODUCTS_URL)
    suspend fun getProducts(
    ): Response<List<Product>>

    @Headers("Content-Type: application/json")
    @GET(EXTERNAL_SUPPLIERS_URL)
    suspend fun getSuppliers(
    ): Response<List<Supplier>>

    @Headers("Content-Type: application/json")
    @GET("${EXTERNAL_PRODUCTS_URL}/{supplier_id}")
    suspend fun getExternalProducts(
        @Path("supplier_id") supplierId: Int
    ): Response<List<Product>>

    @Headers("Content-Type: application/json")
    @POST(TRANSACTIONS_URL)
    suspend fun createPurchase(
        @Body transaction: Transaction
    ): Response<NetworkResponse>

    @Headers("Content-Type: application/json")
    @GET(USERS_URL)
    suspend fun getUsers(
    ): Response<List<User>>

}