package com.example.saxiinventory.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saxiinventory.common.Result
import com.example.saxiinventory.data.repository.ProductRepository
import com.example.saxiinventory.domain.model.NetworkResponse
import com.example.saxiinventory.domain.model.Product
import com.example.saxiinventory.domain.model.Supplier
import com.example.saxiinventory.domain.model.Transaction
import com.example.saxiinventory.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import timber.log.Timber

data class ProductState(
    val isLoading: Boolean = false,
    val data: List<Product> = emptyList(),
    val productsTotal: Int = 0,
    val productCategoryTotal: Int = 0,
    val error: String? = null
)

data class SupplierState(
    val isLoading: Boolean = false,
    val data: List<Supplier> = emptyList(),
    val suppliersTotal: Int = 0,
    val error: String? = null
)

data class UserState(
    val isLoading: Boolean = false,
    val data: List<User> = emptyList(),
    val error: String? = null
)

data class TransactionState(
    val isLoading: Boolean = false,
    val data: NetworkResponse? = null,
    val error: String? = null
)

class ProductViewModel(private val productRepository: ProductRepository) : ViewModel() {

    private val _productsState: MutableStateFlow<ProductState> = MutableStateFlow(ProductState())
    val productsState = _productsState.asStateFlow()

    private val _suppliersState: MutableStateFlow<SupplierState> = MutableStateFlow(SupplierState())
    val suppliersState = _suppliersState.asStateFlow()

    private val _usersState: MutableStateFlow<UserState> = MutableStateFlow(UserState())
    val usersState = _usersState.asStateFlow()

    private val _transactionState: MutableStateFlow<TransactionState> =
        MutableStateFlow(TransactionState())
    val transactionState = _transactionState.asStateFlow()

    fun reset() {
        _productsState.update { ProductState() }
        _suppliersState.update { SupplierState() }
        _transactionState.update { TransactionState() }
    }

    fun fetchProducts() {
        productRepository.getFlowProducts().onEach { result ->
            when (result) {
                is Result.Error -> {
                    _productsState.update {
                        it.copy(
                            isLoading = false,
                            error = "Unable to fetch products."
                        )
                    }
                    Timber.tag("EricTest").d("Error")
                }

                is Result.Loading -> {
                    Timber.tag("EricTest").d("Loading")
                    _productsState.update { it.copy(isLoading = true) }
                }

                is Result.Success -> {
                    Timber.tag("EricTest").d("Success")
                    _productsState.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            data = result.data ?: emptyList(),
                            productsTotal = result.data?.size ?: 0,
                            productCategoryTotal = result.data?.distinctBy { product -> product.category }?.size
                                ?: 0
                        )
                    }
                }
            }

        }.launchIn(viewModelScope)
    }


    fun fetchSuppliers() {
        productRepository.getFlowSuppliers().onEach { result ->
            when (result) {
                is Result.Error -> {
                    _suppliersState.update {
                        it.copy(
                            isLoading = false,
                            error = "Unable to fetch suppliers."
                        )
                    }
                }

                is Result.Loading -> {
                    _suppliersState.update { it.copy(isLoading = true) }
                }

                is Result.Success -> {
                    _suppliersState.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            data = result.data ?: emptyList(),
                            suppliersTotal = result.data?.size ?: 0,
                        )
                    }
                }
            }

        }.launchIn(viewModelScope)
    }

    fun fetchUsers() {
        productRepository.getFlowUsers().onEach { result ->
            when (result) {
                is Result.Error -> {
                    _usersState.update {
                        it.copy(
                            isLoading = false,
                            error = "Unable to fetch users."
                        )
                    }
                }

                is Result.Loading -> {
                    _usersState.update { it.copy(isLoading = true) }
                }

                is Result.Success -> {
                    _usersState.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            data = result.data ?: emptyList()
                        )
                    }
                }
            }

        }.launchIn(viewModelScope)
    }

    fun fetchExternalProducts(supplierId: Int) {
        productRepository.getFlowExternalProducts(supplierId).onEach { result ->
            when (result) {
                is Result.Error -> {
                    _productsState.update {
                        it.copy(
                            isLoading = false,
                            error = "Unable to fetch products."
                        )
                    }
                }

                is Result.Loading -> {
                    _productsState.update { it.copy(isLoading = true) }
                }

                is Result.Success -> {
                    _productsState.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            data = result.data ?: emptyList(),
                            productsTotal = result.data?.size ?: 0,
                            productCategoryTotal = result.data?.distinctBy { product -> product.category }?.size
                                ?: 0
                        )
                    }
                }
            }

        }.launchIn(viewModelScope)
    }


    fun createTransaction(transaction: Transaction) {
        productRepository.createPurchase(transaction).onEach { result ->
            when (result) {
                is Result.Error -> {
                    _transactionState.update {
                        it.copy(
                            isLoading = false,
                            error = "Unable to fetch products."
                        )
                    }
                }

                is Result.Loading -> {
                    _transactionState.update { it.copy(isLoading = true) }
                }

                is Result.Success -> {
                    _transactionState.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            data = result.data
                        )
                    }
                }
            }

        }.launchIn(viewModelScope)
    }
}