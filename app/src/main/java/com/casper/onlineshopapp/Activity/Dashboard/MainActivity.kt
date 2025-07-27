package com.casper.onlineshopapp.Activity.Dashboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.casper.onlineshopapp.Activity.BaseActivity
import com.casper.onlineshopapp.Domain.CategoryModel
import com.casper.onlineshopapp.Domain.ItemModel
import com.casper.onlineshopapp.Domain.SliderModel
import com.casper.onlineshopapp.R
import com.casper.onlineshopapp.ViewModel.MainViewModel
import com.casper.onlineshopapp.ui.theme.OnlineShopAppTheme

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OnlineShopAppTheme {
                Scaffold(
                ) { innerPadding ->
                    DashboardScreen(
                        modifier = Modifier.padding(innerPadding),
                        onCartClick = {}
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier, // Add modifier parameter
    onCartClick: () -> Unit
) {
    val viewModel = MainViewModel()

    val banners = remember { mutableStateListOf<SliderModel>() }
    val categories = remember { mutableStateListOf<CategoryModel>() }
    val bestSeller = remember { mutableStateListOf<ItemModel>() }

    var showBannerLoading by remember { mutableStateOf(true) }
    var showCategoryLoading by remember { mutableStateOf(true) }
    var showBestSellerLoading by remember { mutableStateOf(true) }

    // banner
    LaunchedEffect(Unit) {
        viewModel.loadBanner().observeForever {
            banners.clear()
            banners.addAll(it)
            showBannerLoading = false
        }
    }

    // category
    LaunchedEffect(Unit) {
        viewModel.loadCategory().observeForever {
            categories.clear()
            categories.addAll(it)
            showCategoryLoading = false
        }
    }

    // best seller
    LaunchedEffect(Unit) {
        viewModel.loadBestSeller().observeForever {
            bestSeller.clear()
            bestSeller.addAll(it)
            showBestSellerLoading = false
        }
    }

    ConstraintLayout(
        // Apply the modifier passed from Scaffold here
        modifier = modifier
            .fillMaxSize() // fillMaxSize is appropriate here
            .background(Color.White)
    ) {
        val (scrollList, bottomMenu) = createRefs()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize() // This LazyColumn will respect the padding from its parent (ConstraintLayout)
                .constrainAs(scrollList) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom) // Consider if bottomMenu will overlap or if padding handles it
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth() // Changed from fillMaxSize
                        .padding(top = 70.dp) // This top padding might need adjustment if you add a TopAppBar
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Welcome Back", color = Color.Black)
                        Text(
                            text = "Jackie",
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row { // Removed extra ()
                        Image(
                            painter = painterResource(R.drawable.search_icon),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Image(
                            painter = painterResource(R.drawable.bell_icon),
                            contentDescription = null
                        )
                    }
                }
            }

            //Banners
            item {
                if (showBannerLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth() // Changed from fillMaxSize
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    Banners(banners)
                }
            }

            // Categories
            item {
                Text(
                    text = "Categories",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                        .padding(horizontal = 16.dp)
                )
            }

            item {
                if (showCategoryLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    CategoryList(categories)
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Best Seller Product",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "See All",
                        color = colorResource(R.color.midBrown),
                    )
                }
            }

            item {
                if (showBestSellerLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    ListItems(bestSeller)

                }
            }
        }

        // Bottom Navigation Menu
        BottomMenu(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(bottomMenu) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            onItemClick = onCartClick
        )
    }
}
