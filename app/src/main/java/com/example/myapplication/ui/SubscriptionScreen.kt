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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import com.example.myapplication.Model.Response.ValidateCouponResponse

private val Green = Color(0xFF2E7D32)
private val LightGreen = Color(0xFFE8F5E9)
private val BorderGray = Color(0xFFE0E0E0)

data class SubscriptionPlan(
    val years: Int,
    val price: Int
)

data class ShippingAddress(
    val fullName: String,
    val mobile: String,
    val addressLine: String,
    val landmark: String,
    val city: String,
    val state: String,
    val pincode: String
)

@Composable
fun SubscriptionScreen(
    onSubscribe: (
        magazineType: String,
        plan: SubscriptionPlan,
        couponCode: String,
        shippingAddress: ShippingAddress?
    ) -> Unit,
    onApplyCoupon: (
        couponCode: String,
        magazineType: String,
        plan: SubscriptionPlan
    ) -> Unit,
    onCouponChanged: () -> Unit,
    couponResult: ValidateCouponResponse?,
    isLoading: Boolean
)  {

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

    var couponCode by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var addressLine by remember { mutableStateOf("") }
    var landmark by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }

    val plans = listOf(
        SubscriptionPlan(1, 750),
        SubscriptionPlan(3, 2200),
        SubscriptionPlan(5, 3700)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 14.dp, vertical = 20.dp)
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
                if (selectedType != it) {
                    selectedType = it
                    onCouponChanged()
                }
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
                        if (selectedPlan != plan) {
                            selectedPlan = plan
                            onCouponChanged()
                        }
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
        // COUPON CODE
        // --------------------------------
        Text(
            text = "Coupon Code",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = couponCode,
                onValueChange = {
                    couponCode = it
                    onCouponChanged()
                },
                modifier = Modifier.weight(1f),
                singleLine = true,
                placeholder = { Text("Enter coupon code") },
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Green,
                    unfocusedBorderColor = BorderGray
                )
            )

            Button(
                onClick = {
                    onApplyCoupon(
                        couponCode.trim(),
                        selectedType,
                        selectedPlan
                    )
                },
                enabled = !isLoading && couponCode.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Green
                )
            ) {
                Text("Apply")
            }
        }

        if (couponResult?.valid == true) {
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = LightGreen
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Coupon Applied Successfully",
                        color = Green,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Original Amount: ${couponResult.currency} ${couponResult.original_amount}"
                    )

                    Text(
                        text = "Discount: ${couponResult.currency} ${couponResult.discount}"
                    )

                    Text(
                        text = "Final Amount: ${couponResult.currency} ${couponResult.final_amount}",
                        fontWeight = FontWeight.Bold,
                        color = Green
                    )
                }
            }
        }

        // Shipping address is required only for print subscriptions.
        if (selectedType == "Print Magazine") {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Shipping Address",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))

            AddressTextField("Full Address", fullName) { fullName = it }
            AddressTextField("Mobile number", mobile) { value ->
                mobile = value.filter(Char::isDigit).take(10)
            }
            AddressTextField("House no. / Street / Address", addressLine) { addressLine = it }
            AddressTextField("Landmark (optional)", landmark) { landmark = it }
            AddressTextField("City / District", city) { city = it }
            AddressTextField("State", state) { state = it }
            AddressTextField("PIN code", pincode) { value ->
                pincode = value.filter(Char::isDigit).take(6)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --------------------------------
        // SUBSCRIBE BUTTON
        // --------------------------------

        Button(
            onClick = {
                val shippingAddress = if (selectedType == "Print Magazine") {
                    if (
                        fullName.isBlank() ||
                        mobile.length != 10 ||
                        addressLine.isBlank() ||
                        city.isBlank() ||
                        state.isBlank() ||
                        pincode.length != 6
                    ) {
                        null
                    } else {
                        ShippingAddress(
                            fullName = fullName.trim(),
                            mobile = mobile,
                            addressLine = addressLine.trim(),
                            landmark = landmark.trim(),
                            city = city.trim(),
                            state = state.trim(),
                            pincode = pincode
                        )
                    }
                } else {
                    null
                }

                val addressIsValid =
                    selectedType != "Print Magazine" || shippingAddress != null

                if (addressIsValid) {
                    onSubscribe(
                        selectedType,
                        selectedPlan,
                        couponCode.trim(),
                        shippingAddress
                    )
                }
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Green
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp,
                    color = Color.White
                )
            } else {
                Text(
                    text = "Subscribe Now",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        /*Button(
            onClick = {
                val shippingAddress = if (selectedType == "Print Magazine") {
                    if (
                        fullName.isBlank() ||
                        mobile.length != 10 ||
                        addressLine.isBlank() ||
                        city.isBlank() ||
                        state.isBlank() ||
                        pincode.length != 6
                    ) {
                        null
                    } else {
                        ShippingAddress(
                            fullName = fullName.trim(),
                            mobile = mobile,
                            addressLine = addressLine.trim(),
                            landmark = landmark.trim(),
                            city = city.trim(),
                            state = state.trim(),
                            pincode = pincode
                        )
                    }
                } else {
                    null
                }

                // Do not continue to payment when required print-address fields are missing.
                val addressIsValid = selectedType != "Print Magazine" || shippingAddress != null
                if (addressIsValid) {
                    onCheckoutDetails(couponCode.trim(), shippingAddress)
                    onSubscribe(selectedType, selectedPlan)
                }
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
        }*/
    }
}

@Composable
private fun AddressTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        label = { Text(label) },
        maxLines = 4,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Green,
            unfocusedBorderColor = BorderGray
        )
    )
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
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) Color.White else Color.DarkGray,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis
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
            .height(160.dp)
            .clickable { onClick() }
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) Green else BorderGray,
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) LightGreen else Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (selected) 4.dp else 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp, horizontal = 4.dp),
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
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "POPULAR",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(14.dp))
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${plan.years} YEAR",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (selected) Green else Color.DarkGray,
                maxLines = 1,
                softWrap = false
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "₹${plan.price}",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 1,
                softWrap = false
            )

            Text(
                text = "₹$perYear / yr",
                fontSize = 10.sp,
                color = Color.Gray,
                maxLines = 1,
                softWrap = false
            )

            Spacer(modifier = Modifier.height(2.dp))

            if (saving > 0) {
                Text(
                    text = "Save ₹$saving",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Green,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    softWrap = false
                )
            } else {
                Text(
                    text = "Standard",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    softWrap = false
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            if (selected) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Green,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "Selected",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Green,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(12.dp))
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
