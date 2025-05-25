package com.example.saxiinventory

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.saxiinventory.domain.model.NetworkResponse
import com.example.saxiinventory.domain.model.Product
import com.example.saxiinventory.domain.model.Transaction
import com.example.saxiinventory.domain.model.TransactionDetails
import com.example.saxiinventory.domain.model.User
import com.example.saxiinventory.presentation.viewmodel.ProductViewModel
import com.example.saxiinventory.ui.theme.SaxiInventoryTheme
import com.google.gson.Gson
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransactionActivity : ComponentActivity() {

    private val productViewModel: ProductViewModel by viewModel()

    private val product by lazy {
        val bundle = intent.extras
        bundle?.getString("product", null)?.let {
            Gson().fromJson(it, Product::class.java)
        }
    }

    private val supplierName by lazy {
        val bundle = intent.extras
        bundle?.getString("supplier_name", null)
    }


    private val transactionType by lazy {
        val bundle = intent.extras
        bundle?.getInt("transaction_type", PURCHASE)
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SaxiInventoryTheme {
                Scaffold(
                    topBar = { SimpleToolbar(title = "Transaction") }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        product?.let {
                            when (transactionType) {
                                PURCHASE -> {
                                    PurchaseView(
                                        productViewModel, it,
                                        { product, quantity, totalAmount ->
                                            val transaction = Transaction(
                                                transactionType = "purchase",
                                                product = product.toTransactionProduct(),
                                                userId = 1,
                                                transactionDetails = TransactionDetails(
                                                    supplierId = product.supplierId,
                                                    name = supplierName ?: "",
                                                    quantity = quantity,
                                                    totalAmount = totalAmount,
                                                    paymentMethod = "bank_transfer",
                                                    expectedDeliveryDate = "2025-06-10",
                                                    customerName = null,
                                                    customerId = null
                                                )
                                            )
                                            productViewModel.createTransaction(transaction)
                                        })
                                }

                                SELL -> {
                                    SellView(
                                        productViewModel,
                                        it,
                                    ) { product, quantity, totalAmount, customer ->
                                        val transaction = Transaction(
                                            transactionType = "sell",
                                            product = product.toTransactionProduct(),
                                            userId = 1,
                                            transactionDetails = TransactionDetails(
                                                customerId = customer?.userId,
                                                customerName = customer?.name,
                                                quantity = quantity,
                                                totalAmount = totalAmount,
                                                paymentMethod = "bank_transfer",
                                                expectedDeliveryDate = null,
                                                supplierId = null,
                                                name = null
                                            )
                                        )
                                        productViewModel.createTransaction(transaction)
                                    }
                                }

                                else -> {
                                    /*Do nothing*/
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun PurchaseView(
        productViewModel: ProductViewModel,
        product: Product,
        onPurchaseConfirmClick: (Product, Int, Double) -> Unit
    ) {
        val transactionState by productViewModel.transactionState.collectAsState()
        var quantity by remember { mutableStateOf(1) }
        val totalPrice = product.price * quantity
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White)
        ) {
            Text(text = "Purchase ${product.name}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Price per unit: $${product.price}", fontSize = 16.sp, color = Color.Blue)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { if (quantity > 1) quantity-- }) {
                    Text("-")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Quantity: $quantity", fontSize = 18.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = { quantity++ }) {
                    Text("+")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Total Price: $${totalPrice}", fontSize = 18.sp, color = Color.Magenta)
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { onPurchaseConfirmClick(product, quantity, totalPrice) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirm Purchase")
            }
        }

        when {
            transactionState.isLoading -> LoadingScreen()
            transactionState.error != null -> Text("Error making transaction, please try again later.")
            else -> {
                transactionState.data?.let {
                    TransactionScreen(it)
                }
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SellView(
        productViewModel: ProductViewModel,
        product: Product,
        onSellConfirmClick: (Product, Int, Double, User) -> Unit
    ) {
        var customers by remember { mutableStateOf<List<String>>(emptyList()) }
        val transactionState by productViewModel.transactionState.collectAsState()
        val userState by productViewModel.usersState.collectAsState()
        var quantity by remember { mutableStateOf(1) }
        var selectedCustomer by remember { mutableStateOf(customers.firstOrNull() ?: "") }
        var selectedCustomerObject by remember { mutableStateOf(userState.data.find { it.name == selectedCustomer }) }
        val totalPrice = product.price * quantity
        var expanded by remember { mutableStateOf(false) }
        when {
            userState.isLoading -> LoadingScreen()
            userState.error != null -> Text("Error: ${userState.error}")
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .background(Color.White)
                ) {
                    Text(
                        text = "Sell ${product.name}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Selling Price per unit: $${product.price}",
                        fontSize = 16.sp,
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it }
                    ) {
                        TextField(
                            value = selectedCustomer,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select Customer") },
                            modifier = Modifier.menuAnchor(),
                            trailingIcon = {
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = null
                                )
                            }
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            customers.forEach { customer ->
                                DropdownMenuItem(
                                    text = { Text(text = customer) },
                                    onClick = {
                                        selectedCustomer = customer
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(onClick = { if (quantity > 1) quantity-- }) {
                            Text("-")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "Quantity: $quantity", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(onClick = { quantity++ }) {
                            Text("+")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Total Revenue: $${totalPrice}",
                        fontSize = 18.sp,
                        color = Color.Magenta
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            onSellConfirmClick(product, quantity, totalPrice, selectedCustomerObject!!)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Confirm Sell")
                    }
                }
            }
        }
        when {
            transactionState.isLoading -> LoadingScreen()
            transactionState.error != null -> Text("Error making transaction, please try again later.")
            else -> {
                transactionState.data?.let {
                    TransactionScreen(it)
                }
            }
        }
        LaunchedEffect(Unit) {
            productViewModel.fetchUsers()
        }
        LaunchedEffect(userState) {
            customers = userState.data.map { it.name }
        }
        LaunchedEffect(selectedCustomer) {
            selectedCustomerObject = userState.data.find { it.name == selectedCustomer }
        }
    }


    @Composable
    fun TransactionScreen(networkResponse: NetworkResponse) {
        val response = NetworkResponse(
            "Your purchase was successful!",
            transactionId = networkResponse.transactionId
        )
        var showDialog by remember { mutableStateOf(true) }

        if (showDialog) {
            AutoDismissSuccessDialog(response = response, onDismiss = {
                showDialog = false
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            })
        }
    }

    @Composable
    fun AutoDismissSuccessDialog(response: NetworkResponse, onDismiss: () -> Unit) {
        LaunchedEffect(Unit) {
            delay(3000) // Auto-dismiss after 3 seconds
            onDismiss()
        }
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "Transaction Successful", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(text = response.message, fontSize = 16.sp, color = Color.Black)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Transaction ID: ${response.transactionId}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Green
                    )
                }
            },
            confirmButton = {
                Button(onClick = onDismiss) {
                    Text("OK")
                }
            }
        )
    }

    companion object {
        const val PURCHASE = 0
        const val SELL = 1
        const val RETURN = 2
    }
}





