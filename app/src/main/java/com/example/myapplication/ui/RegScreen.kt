package com.example.myapplication.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.text.KeyboardOptions
import com.example.myapplication.Model.Request.RegistrationRequest

@Composable
fun RegistrationScreen(
    onRegisterClick: (RegistrationRequest) -> Unit
) {
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var mobile by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var fullAddress by rememberSaveable { mutableStateOf("") }
    var selectedState by rememberSaveable { mutableStateOf("") }
    var selectedDistrict by rememberSaveable { mutableStateOf("") }
    var selectedMandal by rememberSaveable { mutableStateOf("") }

    var selectedCrops by rememberSaveable {
        mutableStateOf(setOf<String>())
    }

    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val crops = listOf(
        "🌾 Paddy",
        "🌽 Maize",
        "🌶️ Chilli",
        "🥜 Groundnut",
        "🍅 Tomato",
        "🫘 Pulses",
        "🍌 Banana",
        "🥭 Mango"
    )

    val states = listOf(
        "Andhra Pradesh",
        "Telangana",
        "Karnataka",
        "Tamil Nadu"
    )

    val districts = when (selectedState) {
        "Andhra Pradesh" -> listOf(
            "Visakhapatnam",
            "Vijayawada",
            "Guntur",
            "Tirupati"
        )

        "Telangana" -> listOf(
            "Hyderabad",
            "Warangal",
            "Nalgonda"
        )

        else -> emptyList()
    }

    var pincode by rememberSaveable {
        mutableStateOf("")
    }

    val mandals = when (selectedDistrict) {
        "Visakhapatnam" -> listOf(
            "Anakapalle",
            "Bheemunipatnam",
            "Gajuwaka"
        )

        "Vijayawada" -> listOf(
            "Vijayawada Rural",
            "Gannavaram"
        )

        "Hyderabad" -> listOf(
            "Amberpet",
            "Khairatabad",
            "Secunderabad"
        )

        else -> emptyList()
    }

    Scaffold(
        containerColor = Color(0xFFF6F9F4),
        bottomBar = {
            Surface(
                tonalElevation = 6.dp,
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                Button(
                    onClick = {
                        onRegisterClick(
                            RegistrationRequest(
                                firstName = firstName,
                                lastName = lastName,
                                phone = mobile,
                                email = email.ifBlank { null },
                                password = password,
                                state = selectedState,
                                district = selectedDistrict,
                                mandal = selectedMandal,
                                pincode = pincode,
                                crop_interests = selectedCrops
                                    .toList()
                                    .takeIf { it.isNotEmpty() }
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 20.dp,
                            vertical = 12.dp
                        )
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2E7D32)
                    )
                ) {
                    Text(
                        text = "Create Account",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {

            // ------------------------------------------------
            // HEADER
            // ------------------------------------------------

            item {
                AgricultureHeader()
            }

            // ------------------------------------------------
            // PERSONAL DETAILS
            // ------------------------------------------------

            item {
                SectionTitle(
                    icon = "👤",
                    title = "Personal Details",
                    subtitle = "Tell us a little about yourself"
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 1.dp
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            RoundedTextField(
                                value = firstName,
                                onValueChange = {
                                    firstName = it
                                },
                                label = "First name",
                                modifier = Modifier.weight(1f)
                            )

                            RoundedTextField(
                                value = lastName,
                                onValueChange = {
                                    lastName = it
                                },
                                label = "Last name",
                                modifier = Modifier.weight(1f)
                            )
                        }

                        RoundedTextField(
                            value = mobile,
                            onValueChange = {
                                if (it.length <= 10) {
                                    mobile = it.filter { char ->
                                        char.isDigit()
                                    }
                                }
                            },
                            label = "Mobile number",
                            leadingIcon = Icons.Default.Phone,
                            keyboardType = KeyboardType.Phone
                        )

                        RoundedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                            },
                            label = "Email (optional)",
                            leadingIcon = Icons.Default.Email,
                            keyboardType = KeyboardType.Email
                        )

                        RoundedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                            },
                            label = "Password",
                            leadingIcon = Icons.Default.Lock,
                            visualTransformation =
                                if (passwordVisible)
                                    VisualTransformation.None
                                else
                                    PasswordVisualTransformation(),
                            trailingIcon = {

                                TextButton(
                                    onClick = {
                                        passwordVisible = !passwordVisible
                                    }
                                ) {
                                    Text(
                                        text = if (passwordVisible) "Hide" else "Show",
                                        color = Color(0xFF2E7D32)
                                    )
                                }
                               /* IconButton(
                                    onClick = {
                                        passwordVisible =
                                            !passwordVisible
                                    }
                                ) {
                                    Icon(
                                        imageVector =
                                            if (passwordVisible)
                                                Icons.Default.Visibility
                                            else
                                                Icons.Default.VisibilityOff,
                                        contentDescription =
                                            "Toggle password visibility"
                                    )
                                }*/
                            }
                        )
                    }
                }
            }

            // ------------------------------------------------
            // LOCATION
            // ------------------------------------------------

            item {
                Spacer(modifier = Modifier.height(18.dp))

                SectionTitle(
                    icon = "📍",
                    title = "Farm Location",
                    subtitle = "Where is your farm located?"
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        DropdownField(
                            value = selectedState,
                            label = "State",
                            options = states,
                            onSelected = {
                                selectedState = it
                                selectedDistrict = ""
                                selectedMandal = ""
                            }
                        )

                        DropdownField(
                            value = selectedDistrict,
                            label = "District",
                            options = districts,
                            enabled = selectedState.isNotEmpty(),
                            onSelected = {
                                selectedDistrict = it
                                selectedMandal = ""
                            }
                        )

                        DropdownField(
                            value = selectedMandal,
                            label = "Mandal",
                            options = mandals,
                            enabled = selectedDistrict.isNotEmpty(),
                            onSelected = {
                                selectedMandal = it
                            }
                        )

                        RoundedTextField(
                            value = pincode,
                            onValueChange = {

                                if (it.length <= 6) {
                                    pincode = it.filter { char ->
                                        char.isDigit()
                                    }
                                }

                            },
                            label = "Pincode",
                            keyboardType = KeyboardType.Number
                        )
                    }
                }
            }

            // ------------------------------------------------
            // CROP INTEREST
            // ------------------------------------------------

            /*item {
                Spacer(modifier = Modifier.height(18.dp))

                SectionTitle(
                    icon = "🌾",
                    title = "Crop Interests",
                    subtitle = "Select the crops you're interested in"
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        FlowRow(
                            horizontalArrangement =
                                Arrangement.spacedBy(8.dp),
                            verticalArrangement =
                                Arrangement.spacedBy(8.dp)
                        ) {

                            crops.forEach { crop ->

                                val selected =
                                    selectedCrops.contains(crop)

                                FilterChip(
                                    selected = selected,
                                    onClick = {

                                        selectedCrops =
                                            if (selected) {
                                                selectedCrops - crop
                                            } else {
                                                selectedCrops + crop
                                            }
                                    },
                                    label = {
                                        Text(
                                            text = crop,
                                            fontSize = 14.sp
                                        )
                                    },
                                    shape = RoundedCornerShape(50),
                                    leadingIcon = if (selected) {
                                        {
                                            Icon(
                                                imageVector =
                                                    Icons.Default.Check,
                                                contentDescription = null,
                                                modifier =
                                                    Modifier.size(18.dp)
                                            )
                                        }
                                    } else {
                                        null
                                    }
                                )
                            }
                        }
                    }
                }
            }*/

            item {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "By creating an account, you agree to our Terms & Privacy Policy.",
                    modifier = Modifier.padding(horizontal = 20.dp),
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    value: String,
    label: String,
    options: List<String>,
    enabled: Boolean = true,
    onSelected: (String) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            if (enabled) {
                expanded = !expanded
            }
        }
    ) {

        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            label = {
                Text(label)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(14.dp),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor =
                    Color(0xFF2E7D32),
                focusedLabelColor =
                    Color(0xFF2E7D32)
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {

            options.forEach { option ->

                DropdownMenuItem(
                    text = {
                        Text(option)
                    },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun RoundedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation =
        VisualTransformation.None
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = {
            Text(label)
        },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = null
                )
            }
        },
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        visualTransformation = visualTransformation,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF2E7D32),
            focusedLabelColor = Color(0xFF2E7D32),
            cursorColor = Color(0xFF2E7D32)
        )
    )
}

@Composable
fun SectionTitle(
    icon: String,
    title: String,
    subtitle: String
) {

    Column(
        modifier = Modifier.padding(
            start = 20.dp,
            end = 20.dp,
            top = 4.dp,
            bottom = 10.dp
        )
    ) {

        Row(
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text = icon,
                fontSize = 22.sp
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF263328)
            )
        }

        Text(
            text = subtitle,
            modifier = Modifier.padding(
                start = 30.dp
            ),
            fontSize = 13.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun AgricultureHeader() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .clip(
                RoundedCornerShape(
                    bottomStart = 32.dp,
                    bottomEnd = 32.dp
                )
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1B5E20),
                        Color(0xFF43A047)
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 24.dp,
                    vertical = 28.dp
                ),
            verticalArrangement =
                Arrangement.Center
        ) {

            Text(
                text = "🌱",
                fontSize = 42.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Join the Eruvaaka Community",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Create your account and grow smarter.",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp
            )
        }
    }
}

data class RegistrationData(
    val firstName: String,
    val lastName: String,
    val mobile: String,
    val email: String,
    val password: String,
    val state: String,
    val district: String,
    val mandal: String,
    val pincode:String,
    val crops: List<String>
)