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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.saxiinventory.domain.model.HomeDashBoardItemType
import com.example.saxiinventory.domain.model.HomeDashBoardModel
import com.example.saxiinventory.presentation.viewmodel.ProductViewModel
import com.example.saxiinventory.ui.theme.SaxiInventoryTheme
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : ComponentActivity() {


    private val productViewModel: ProductViewModel by viewModel()

    private val homeDashBoardModels: List<HomeDashBoardModel> = arrayListOf(
        HomeDashBoardModel(HomeDashBoardItemType.PRODUCTS, "Total Products", "0"),
        HomeDashBoardModel(HomeDashBoardItemType.PRODUCT_CATEGORY, "Product Category", "0"),
        HomeDashBoardModel(HomeDashBoardItemType.SUPPLIERS, "Total Suppliers", "0")
    )

    private val homeDashBoardModel: MutableStateFlow<List<HomeDashBoardModel>> =
        MutableStateFlow(homeDashBoardModels)


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SaxiInventoryTheme {
                Scaffold(
                    topBar = { SimpleToolbar(title = "Saxi Inventory") }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues), // Apply padding here
                        contentAlignment = Alignment.Center
                    ) {
                        GridScreen(productViewModel)
                    }
                }
            }
        }
    }

    override fun onPause() {
        Timber.tag("EricTest").d("onResume()")
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        Timber.tag("EricTest").d("onResume()")
        productViewModel.fetchProducts()
        productViewModel.fetchSuppliers()
    }

    @Composable
    fun GridScreen(productViewModel: ProductViewModel) {
        val productState by productViewModel.productsState.collectAsState()
        val suppliersState by productViewModel.suppliersState.collectAsState()
        val homeDashBoardModel by homeDashBoardModel.collectAsState()
        when {
            productState.isLoading || suppliersState.isLoading -> LoadingScreen()
            productState.error != null || suppliersState.error != null -> Text("Error: ${productState.error}")
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2), // Defines a 2-column grid
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(homeDashBoardModel.size) { index ->
                        GridItem(homeDashBoardModel[index])
                    }
                }
            }
        }
        LaunchedEffect(productState) {
            homeDashBoardModel.find { it.type == HomeDashBoardItemType.PRODUCTS }?.quantity =
                (productState.productsTotal).toString()
            homeDashBoardModel.find { it.type == HomeDashBoardItemType.PRODUCT_CATEGORY }?.quantity =
                (productState.productCategoryTotal).toString()
        }
        LaunchedEffect(suppliersState) {
            homeDashBoardModel.find { it.type == HomeDashBoardItemType.SUPPLIERS }?.quantity =
                (suppliersState.suppliersTotal).toString()
        }
    }

    @Composable
    fun GridItem(homeDashBoardModel: HomeDashBoardModel) {
        Timber.tag("EricTest").d("${homeDashBoardModel.title} : ${homeDashBoardModel.quantity}")
        Box(
            modifier = Modifier
                .aspectRatio(1f) // Ensures square shape
                .padding(8.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .clickable {
                    when (homeDashBoardModel.type) {
                        HomeDashBoardItemType.PRODUCTS -> {
                            startActivity(Intent(this@MainActivity, ProductActivity::class.java))
                        }

                        HomeDashBoardItemType.PRODUCT_CATEGORY -> {
                            Toast.makeText(
                                this@MainActivity,
                                "This feature is under development, please try again next time.",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        HomeDashBoardItemType.SUPPLIERS -> {
                            startActivity(Intent(this@MainActivity, SuppliersActivity::class.java))
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = homeDashBoardModel.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp)) // Adds spacing
                Text(
                    text = "Quantity: ${homeDashBoardModel.quantity}",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleToolbar(title: String) {
    TopAppBar(
        title = { Text(title, color = Color.LightGray) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.DarkGray)
    )
}