package com.example.saxiinventory

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.saxiinventory.TransactionActivity.Companion.PURCHASE
import com.example.saxiinventory.TransactionActivity.Companion.SELL
import com.example.saxiinventory.domain.model.Product
import com.example.saxiinventory.presentation.viewmodel.ProductViewModel
import com.example.saxiinventory.ui.theme.SaxiInventoryTheme
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductActivity : ComponentActivity() {

    private var count = MutableStateFlow(0)

    private val productViewModel: ProductViewModel by viewModel()

    private val supplierId by lazy {
        val bundle = intent.extras
        bundle?.getInt("supplier_id", -1)
    }
    private val supplierName by lazy {
        val bundle = intent.extras
        bundle?.getString("supplier_name", null)
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val totalCount by count.collectAsState()
            val title = "${(supplierName ?: "Products")} (${totalCount})"
            SaxiInventoryTheme {
                Scaffold(
                    topBar = { SimpleToolbar(title = title) }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues), // Apply padding here
                        contentAlignment = Alignment.Center
                    ) {
                        ProductList(
                            productViewModel,
                            supplierId ?: -1,
                            { product, transactionType ->
                                val intent =
                                    Intent(this@ProductActivity, TransactionActivity::class.java)
                                val bundle = Bundle()
                                bundle.putString("product", Gson().toJson(product))
                                bundle.putString("supplier_name", supplierName)
                                bundle.putInt("transaction_type", transactionType)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            })
                    }
                }
            }
        }
    }


    @Composable
    fun ProductItem(product: Product, supplierId: Int, onPurchaseClick: (Product, Int) -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .clickable { /* Handle click */ }
                .padding(16.dp)
        ) {
            Text(text = product.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = product.description, fontSize = 14.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Price: $${product.price}", fontSize = 16.sp, color = Color.Blue)
            if (supplierId == -1) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Quantity: ${product.quantityAvailable}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { onPurchaseClick(product, SELL) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sell")
                }
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = {
                        Toast.makeText(
                            this@ProductActivity,
                            "This feature is under development, please try again next time.",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Return")
                }
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { onPurchaseClick(product, PURCHASE) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Purchase")
                }
            }
        }
    }

    @Composable
    fun ProductList(
        productViewModel: ProductViewModel,
        supplierId: Int,
        onPurchaseClick: (Product, Int) -> Unit
    ) {
        val productState by productViewModel.productsState.collectAsState()
        when {
            productState.isLoading -> LoadingScreen()
            productState.error != null -> Text("Error: ${productState.error}")
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    for (product in productState.data) {
                        ProductItem(product, supplierId, onPurchaseClick)
                    }
                }
            }
        }
        LaunchedEffect(Unit) {
            if (supplierId == -1) {
                productViewModel.fetchProducts()
            } else {
                productViewModel.fetchExternalProducts(supplierId)
            }
        }
        LaunchedEffect(productState) {
            count.value = productState.data.size
        }
    }
}




