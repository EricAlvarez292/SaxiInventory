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
import com.example.saxiinventory.domain.model.Supplier
import com.example.saxiinventory.presentation.viewmodel.ProductViewModel
import com.example.saxiinventory.ui.theme.SaxiInventoryTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class SuppliersActivity : ComponentActivity() {

    private val productViewModel: ProductViewModel by viewModel()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SaxiInventoryTheme {
                Scaffold(
                    topBar = { SimpleToolbar(title = "Suppliers") }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        SupplierList(productViewModel, {
                            val intent = Intent(this@SuppliersActivity, ProductActivity::class.java)
                            val bundle = Bundle()
                            bundle.putInt("supplier_id", it.supplierId)
                            bundle.putString("supplier_name", it.name)
                            intent.putExtras(bundle)
                            startActivity(intent)
                        })
                    }
                }
            }
        }
    }

    @Composable
    fun SupplierItem(supplier: Supplier, onTransactionClick: (Supplier) -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text(text = supplier.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Contact: ${supplier.contactInfo}",
                fontSize = 14.sp,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Address: ${supplier.address}", fontSize = 14.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { onTransactionClick(supplier) }) {
                Text("Create Transaction")
            }
        }
    }

    @Composable
    fun SupplierList(productViewModel: ProductViewModel, onTransactionClick: (Supplier) -> Unit) {
        val suppliersState by productViewModel.suppliersState.collectAsState()
        when {
            suppliersState.isLoading -> LoadingScreen()
            suppliersState.error != null -> Text("Error: ${suppliersState.error}")
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    for (supplier in suppliersState.data) {
                        SupplierItem(supplier, onTransactionClick)
                    }
                }
            }

        }
        LaunchedEffect(Unit) {
            productViewModel.fetchSuppliers()
        }

    }
}