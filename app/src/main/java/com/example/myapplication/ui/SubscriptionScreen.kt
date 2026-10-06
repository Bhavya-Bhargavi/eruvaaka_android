package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*

private val Green = Color(0xFF2E7D32)
private val LightGreen = Color(0xFFE8F5E9)
private val BorderGray = Color(0xFFE0E0E0)

data class SubscriptionPlan(
    val years: Int,
    val price: Int
)

@Composable
fun SubscriptionScreen(
    onSubscribe: (String, SubscriptionPlan) -> Unit
) {

    var selectedType by remember {
        mutableStateOf("E-Magazine")
    }

    var selectedPlan by remember {
        mutableStateOf(
            SubscriptionPlan(
                years = 3,
                price = 2200
            )
        )
    }

    val plans = listOf(
        SubscriptionPlan(1, 750),
        SubscriptionPlan(3, 2200),
        SubscriptionPlan(5, 3700)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp) //20.dp

    ) {

        // --------------------------------
        // TITLE
        // --------------------------------

        Text(
            text = "Magazine Subscription",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Choose your magazine and subscription plan",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))


        // --------------------------------
        // MAGAZINE TYPE TABS
        // --------------------------------

        MagazineTypeTabs(
            selectedType = selectedType,
            onTypeSelected = {
                selectedType = it
            }
        )

        Spacer(modifier = Modifier.height(24.dp))


        // --------------------------------
        // PLAN TITLE
        // --------------------------------

        Text(
            text = "Choose your plan",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))


        // --------------------------------
        // PLAN CARDS
        // --------------------------------

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            plans.forEach { plan ->

                SubscriptionPlanCard(
                    plan = plan,
                    selected = selectedPlan.years == plan.years,
                    popular = plan.years == 3,
                    onClick = {
                        selectedPlan = plan
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))


        // --------------------------------
        // SELECTED PLAN SUMMARY
        // --------------------------------

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = LightGreen
            ),
            shape = RoundedCornerShape(12.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Green,
                    modifier = Modifier.size(22.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {

                    Text(
                        text = "${selectedPlan.years} Year Plan Selected",
                        fontWeight = FontWeight.Bold,
                        color = Green
                    )

                    Text(
                        text = "₹${selectedPlan.price}",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))


        // --------------------------------
        // SUBSCRIBE BUTTON
        // --------------------------------

        Button(
            onClick = {
                onSubscribe(
                    selectedType,
                    selectedPlan
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Green
            )
        ) {

            Text(
                text = "Subscribe Now",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun MagazineTypeTabs(
    selectedType: String,
    onTypeSelected: (String) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = BorderGray,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(4.dp)
    ) {

        MagazineTab(
            title = "E-Magazine",
            selected = selectedType == "E-Magazine",
            onClick = {
                onTypeSelected("E-Magazine")
            },
            modifier = Modifier.weight(1f)
        )

        MagazineTab(
            title = "Print Magazine",
            selected = selectedType == "Print Magazine",
            onClick = {
                onTypeSelected("Print Magazine")
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun MagazineTab(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .height(44.dp)
            .background(
                color = if (selected) Green else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = if (selected) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            },
            color = if (selected) {
                Color.White
            } else {
                Color.DarkGray
            }
        )
    }
}

@Composable
fun SubscriptionPlanCard(
    plan: SubscriptionPlan,
    selected: Boolean,
    popular: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val yearlyBasePrice = 750

    val normalPrice = yearlyBasePrice * plan.years

    val saving = normalPrice - plan.price

    val perYear = plan.price / plan.years

    Card(
        modifier = modifier
            .clickable {
                onClick()
            }
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) Green else BorderGray,
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                LightGreen
            } else {
                Color.White
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (selected) 4.dp else 1.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Popular badge
            if (popular) {

                Box(
                    modifier = Modifier
                        .background(
                            color = Green,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(
                            horizontal = 7.dp,
                            vertical = 4.dp
                        )
                ) {

                    Text(
                        text = "POPULAR",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

            } else {

                Spacer(
                    modifier = Modifier.height(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${plan.years} YEAR",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = if (selected) Green else Color.DarkGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "₹${plan.price}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = "₹$perYear / year",
                fontSize = 11.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (saving > 0) {

                Text(
                    text = "Save ₹$saving",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Green,
                    textAlign = TextAlign.Center
                )

            } else {

                Text(
                    text = "Standard price",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (selected) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Green,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(3.dp))

                    Text(
                        text = "Selected",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Green
                    )
                }
            }
        }
    }
}


/*
@Composable
fun SubscriptionScreen(
    onSubscribeClick: (String) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = "Choose Your Plan",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                SubscriptionCard(
                    title = "1 Year",
                    price = "₹750",
                    planId = "1_year",
                    highlight = false,
                    onSubscribeClick = onSubscribeClick
                )
            }

            item {
                SubscriptionCard(
                    title = "3 Years",
                    price = "₹2200",
                    planId = "3_years",
                    highlight = true,
                    onSubscribeClick = onSubscribeClick
                )
            }

            item {
                SubscriptionCard(
                    title = "5 Years",
                    price = "₹3700",
                    planId = "5_years",
                    highlight = false,
                    onSubscribeClick = onSubscribeClick
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                SampleDownloadSection()
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        FloatingActionButton(
            onClick = {
                // Open WhatsApp
            },
            containerColor = Color(0xFF25D366),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.whatsapp),
                contentDescription = "WhatsApp",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}*/

@Composable
fun SubscriptionCard(
    title: String,
    price: String,
    planId: String,
    highlight: Boolean,
    onSubscribeClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (highlight) {
                Color(0xFFE8F5E9)
            } else {
                Color.White
            }
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (highlight) {
                Text(
                    text = "BEST VALUE",
                    color = Color(0xFF2E7D32),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = price,
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    onSubscribeClick(planId)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (highlight) {
                        Color(0xFF2E7D32)
                    } else {
                        Color(0xFF1565C0)
                    }
                )
            ) {
                Text(
                    text = "Subscribe Now",
                    color = Color.White
                )
            }
        }
    }
}

/*@Composable
fun SubscriptionScreen(
    onSubscribeClick: (planId: String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = "Choose Your Plan",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                SubscriptionCard(
                    title = "1 Year",
                    price = "₹750",
                    planId = "1_year",
                    highlight = false,
                    onSubscribeClick = onSubscribeClick
                )
            }

            item {
                SubscriptionCard(
                    title = "3 Years",
                    price = "₹2200",
                    planId = "3_years",
                    highlight = true,
                    onSubscribeClick = onSubscribeClick
                )
            }

            item {
                SubscriptionCard(
                    title = "5 Years",
                    price = "₹3700",
                    planId = "5_years",
                    highlight = false,
                    onSubscribeClick = onSubscribeClick
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                SampleDownloadSection()
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        FloatingActionButton(
            onClick = { *//* open WhatsApp *//* },
            containerColor = Color(0xFF25D366),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.whatsapp),
                contentDescription = "WhatsApp",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}*/

/*@Composable
fun SubscriptionCard(
    title: String,
    price: String,
    planId: String,
    highlight: Boolean,
    onSubscribeClick: (planId: String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (highlight) {
                Color(0xFFE8F5E9)
            } else {
                Color.White
            }
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (highlight) {
                Text(
                    text = "BEST VALUE",
                    color = Color(0xFF2E7D32),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = price,
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    onSubscribeClick(planId)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (highlight) {
                        Color(0xFF2E7D32)
                    } else {
                        Color(0xFF1565C0)
                    }
                )
            ) {
                Text(
                    text = "Subscribe Now",
                    color = Color.White
                )
            }
        }
    }
}*/

/*@Composable
fun SubscriptionScreen() {

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
                .padding(16.dp)
        ) {

            item {
                Text(
                    text = "Choose Your Plan",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                SubscriptionCard(
                    title = "1 Year",
                    price = "₹750",
                    highlight = false
                )
            }

            item {
                SubscriptionCard(
                    title = "3 Years",
                    price = "₹2200",
                    highlight = true // ⭐ BEST PLAN
                )
            }

            item {
                SubscriptionCard(
                    title = "5 Years",
                    price = "₹3700",
                    highlight = false
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))

                SampleDownloadSection()
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // ✅ WhatsApp Floating Button
        FloatingActionButton(
            onClick = { *//* open WhatsApp *//* },
            containerColor = Color(0xFF25D366),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.whatsapp),
                contentDescription = "WhatsApp",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}*/

@Composable
fun SubscriptionCard(
    title: String,
    price: String,
    highlight: Boolean
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (highlight)
                Color(0xFFE8F5E9)
            else
                Color.White
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            if (highlight) {
                Text(
                    text = "BEST VALUE",
                    color = Color(0xFF2E7D32),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = price,
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { /* Razorpay */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (highlight)
                        Color(0xFF2E7D32)
                    else
                        Color(0xFF1565C0)
                )
            ) {
                Text("Subscribe Now", color = Color.White)
            }
        }
    }
}

@Composable
fun SampleDownloadSection() {

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "Download Free Sample Magazine",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF1744)
                )
            ) {
                Text("DOWNLOAD NOW", color = Color.White)
            }
        }
    }
}
