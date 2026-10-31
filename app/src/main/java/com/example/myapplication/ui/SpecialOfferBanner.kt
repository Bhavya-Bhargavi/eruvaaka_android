package com.example.myapplication.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val OfferGreen = Color(0xFF176B36)
private val OfferDarkGreen = Color(0xFF104D29)
private val OfferLightGreen = Color(0xFFF0F8DF)
private val OfferYellow = Color(0xFFFFD65A)

@Composable
fun SpecialOfferBanner(
    modifier: Modifier = Modifier,
    title: String = "Get 20% OFF",
    description: String = "on E-Magazine Subscription",
    couponCode: String = "DEV25",
    onSubscribeClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = OfferLightGreen
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFF3F9E8),
                            Color(0xFFDDF0C5),
                            Color(0xFFF7F8D9)
                        )
                    )
                )
        ) {
            // Decorative circles
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 20.dp, y = (-20).dp)
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0x3377AA55))
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 12.dp, y = 25.dp)
                    .size(75.dp)
                    .clip(CircleShape)
                    .background(Color(0x2277AA55))
            )

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Offer label
                Surface(
                    shape = RoundedCornerShape(
                        topEnd = 12.dp,
                        bottomEnd = 12.dp,
                        topStart = 4.dp,
                        bottomStart = 4.dp
                    ),
                    color = Color(0xFFD94B36)
                ) {
                    Text(
                        text = "✦  SPECIAL OFFER",
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        ),
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.7.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: Offer details
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = title,
                            fontSize = 25.sp,
                            lineHeight = 30.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = OfferDarkGreen
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = description,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OfferGreen
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "🌱 Stay Updated • Grow Better",
                            fontSize = 11.sp,
                            color = Color(0xFF466B38)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Right: Illustration made without image files
                    Box(
                        modifier = Modifier.size(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            modifier = Modifier.size(84.dp),
                            shape = CircleShape,
                            color = Color(0xFFD3EAB7)
                        ) {}

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🌿",
                                fontSize = 31.sp
                            )

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = OfferGreen
                            ) {
                                Text(
                                    text = "E-MAGAZINE",
                                    modifier = Modifier.padding(
                                        horizontal = 6.dp,
                                        vertical = 4.dp
                                    ),
                                    color = Color.White,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Discount badge
                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 3.dp, y = (-2).dp),
                            shape = CircleShape,
                            color = OfferYellow
                        ) {
                            Text(
                                text = "20%\nOFF",
                                modifier = Modifier.padding(7.dp),
                                color = OfferDarkGreen,
                                fontSize = 10.sp,
                                lineHeight = 11.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Coupon + subscribe button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = OfferYellow
                    ) {
                        Column(
                            modifier = Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 7.dp
                            )
                        ) {
                            Text(
                                text = "USE COUPON",
                                fontSize = 9.sp,
                                color = OfferDarkGreen,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = couponCode,
                                fontSize = 15.sp,
                                color = OfferDarkGreen,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = onSubscribeClick,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OfferGreen,
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(
                            horizontal = 12.dp,
                            vertical = 10.dp
                        )
                    ) {
                        Text(
                            text = "Subscribe",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "→",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}