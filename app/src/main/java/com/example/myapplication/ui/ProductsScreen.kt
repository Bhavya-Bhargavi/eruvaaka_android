package com.example.myapplication.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myapplication.Model.Response.ProductItem
import com.example.myapplication.ViewModel.ProductViewModel

@Composable
fun ProductsScreen(
    productViewModel: ProductViewModel,
    onSubscribeClick: () -> Unit = {}
) {
    val productsState by productViewModel.productsState.collectAsState()
    val offersState by productViewModel.offersState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        productViewModel.fetchProducts()
        productViewModel.fetchActiveOffers()
    }

    val defaultProducts = listOf(
        ProductItem(
            id = 1,
            title = "Organic Bio-Fertilizer Pack",
            name = "Organic Bio-Fertilizer Pack",
            description = "High efficiency eco-friendly soil enhancer for crop yield.",
            price = "₹499",
            category = "Fertilizers"
        ),
        ProductItem(
            id = 2,
            title = "Hybrid Paddy Seeds (5kg)",
            name = "Hybrid Paddy Seeds (5kg)",
            description = "Disease-resistant premium quality paddy seeds.",
            price = "₹850",
            category = "Seeds"
        ),
        ProductItem(
            id = 3,
            title = "Agricultural Soil Testing Kit",
            name = "Soil Testing Kit",
            description = "Instant NPK & pH soil testing kit for smart farming.",
            price = "₹1,200",
            category = "Tools"
        ),
        ProductItem(
            id = 4,
            title = "Multi-Crop Drip Irrigation Set",
            name = "Drip Irrigation Kit",
            description = "Water saving precision drip irrigation equipment.",
            price = "₹2,500",
            category = "Irrigation"
        )
    )

    val productsResult = productsState
    val productList = if (productsResult?.isSuccess == true) {
        val apiData = productsResult.getOrNull()?.getProductList()
        if (!apiData.isNullOrEmpty()) apiData else defaultProducts
    } else {
        defaultProducts
    }

    val activeOffers = offersState?.getOrNull()?.getOfferList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Text(
            text = "Eruvaaka Agri-Products & Store",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1B5E20),
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
        )

        // Render Active Offer Banner if available from GET /api/offers/active
        val offer = activeOffers?.firstOrNull()
        if (offer != null) {
            SpecialOfferBanner(
                title = offer.title ?: "Special Discount Offer",
                description = offer.description ?: "Active promotional discount on subscription",
                couponCode = offer.coupon_code ?: "SPECIAL25",
                onSubscribeClick = onSubscribeClick
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(productList.size) { index ->
                val product = productList[index]
                ProductCard(product = product, context = context)
            }
        }
    }
}

@Composable
fun ProductCard(
    product: ProductItem,
    context: Context
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clickable {
                if (!product.link.isNullOrBlank()) {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(product.link))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Cannot open product link", Toast.LENGTH_SHORT).show()
                    }
                }
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val img = product.getDisplayImage()
            if (!img.isNullOrBlank()) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(img)
                        .crossfade(true)
                        .build(),
                    contentDescription = product.getDisplayName(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(
                            color = Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = "Product",
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.getDisplayName(),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color.Black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            if (!product.price.isNullOrBlank()) {
                Text(
                    text = product.price,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp)
                )
            }

            if (!product.description.isNullOrBlank()) {
                Text(
                    text = product.description,
                    fontSize = 10.sp,
                    color = Color.DarkGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (!product.link.isNullOrBlank()) {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(product.link))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Cannot open product link", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Inquiring about ${product.getDisplayName()}", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
            ) {
                Text(text = "View / Buy", fontSize = 10.sp, color = Color.White)
            }
        }
    }
}
