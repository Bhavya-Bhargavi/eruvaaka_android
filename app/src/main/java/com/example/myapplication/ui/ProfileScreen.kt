package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ViewModel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onSave: (
        firstName: String,
        lastName: String,
        phone: String,
        email: String,
        postalAddress: String,
        address1: String,
        address2: String,
        state: String,
        district: String,
        mandal: String,
        pincode: String,
        cropInterests: List<String>
    ) -> Unit
) {
    val profile by viewModel.profile.collectAsState()

    var firstName by rememberSaveable { mutableStateOf(profile.first_name.orEmpty()) }
    var lastName by rememberSaveable { mutableStateOf(profile.last_name.orEmpty()) }
    var phone by rememberSaveable { mutableStateOf(profile.phone.orEmpty()) }
    var email by rememberSaveable { mutableStateOf(profile.email.orEmpty()) }
    var postalAddress by rememberSaveable { mutableStateOf(profile.postal_address.ifEmpty { profile.address.orEmpty() }) }
    var address1 by rememberSaveable { mutableStateOf(profile.address1.orEmpty()) }
    var address2 by rememberSaveable { mutableStateOf(profile.address2.orEmpty()) }
    var selectedState by rememberSaveable { mutableStateOf(profile.state.orEmpty()) }
    var selectedDistrict by rememberSaveable { mutableStateOf(profile.district.orEmpty()) }
    var selectedMandal by rememberSaveable { mutableStateOf(profile.mandal.orEmpty()) }
    var pincode by rememberSaveable { mutableStateOf(profile.pincode.orEmpty()) }
    var selectedCrops by rememberSaveable { mutableStateOf(profile.crop_interests ?: emptyList()) }

    LaunchedEffect(profile) {
        if (firstName.isBlank()) firstName = profile.first_name.orEmpty()
        if (lastName.isBlank()) lastName = profile.last_name.orEmpty()
        if (phone.isBlank()) phone = profile.phone.orEmpty()
        if (email.isBlank()) email = profile.email.orEmpty()
        if (postalAddress.isBlank()) postalAddress = profile.postal_address.ifEmpty { profile.address.orEmpty() }
        if (address1.isBlank()) address1 = profile.address1.orEmpty()
        if (address2.isBlank()) address2 = profile.address2.orEmpty()
        if (selectedState.isBlank()) selectedState = profile.state.orEmpty()
        if (selectedDistrict.isBlank()) selectedDistrict = profile.district.orEmpty()
        if (selectedMandal.isBlank()) selectedMandal = profile.mandal.orEmpty()
        if (pincode.isBlank()) pincode = profile.pincode.orEmpty()
        if (selectedCrops.isEmpty()) selectedCrops = profile.crop_interests ?: emptyList()
    }

    val states = listOf(
        "Andhra Pradesh",
        "Telangana",
        "Karnataka",
        "Tamil Nadu",
        "Maharashtra"
    )

    val districts = when (selectedState) {
        "Andhra Pradesh" -> listOf("Visakhapatnam", "Vijayawada", "Guntur", "Tirupati", "Anantapur", "Kurnool", "Kakinada")
        "Telangana" -> listOf("Hyderabad", "Warangal", "Nalgonda", "Karimnagar", "Khammam", "Nizamabad", "Rangareddy")
        "Karnataka" -> listOf("Bengaluru", "Mysuru", "Mangaluru", "Hubballi", "Belagavi")
        "Tamil Nadu" -> listOf("Chennai", "Coimbatore", "Madurai", "Salem", "Tiruchirappalli")
        "Maharashtra" -> listOf("Mumbai", "Pune", "Nagpur", "Nashik", "Aurangabad")
        else -> if (selectedState.isNotBlank()) listOf(selectedDistrict).filter { it.isNotBlank() } else emptyList()
    }

    val mandals = when (selectedDistrict) {
        "Visakhapatnam" -> listOf("Anakapalle", "Bheemunipatnam", "Gajuwaka", "Padmanabham")
        "Vijayawada" -> listOf("Vijayawada Rural", "Gannavaram", "Kanchikacherla", "Penamaluru")
        "Hyderabad" -> listOf("Amberpet", "Khairatabad", "Secunderabad", "Asifnagar", "Bahadurpura")
        "Warangal" -> listOf("Hanamkonda", "Warangal", "Kazipet")
        else -> if (selectedDistrict.isNotBlank()) listOf(selectedMandal).filter { it.isNotBlank() } else emptyList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(12.dp)
    ) {
        Text(
            text = "My Profile",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "${profile.first_name} ${profile.last_name}",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = profile.phone,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(14.dp))

        // ------------------------------------------------
        // 1. PERSONAL & CONTACT INFORMATION
        // ------------------------------------------------
        ProfileSection(title = "Personal & Contact Information") {
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = {
                    if (it.length <= 10) phone = it.filter { c -> c.isDigit() }
                },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // ------------------------------------------------
        // 2. POSTAL ADDRESS & LOCATION
        // ------------------------------------------------
        ProfileSection(title = "Postal Address & Location") {
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = postalAddress,
                onValueChange = { postalAddress = it },
                label = { Text("Postal Address") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
                singleLine = false
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = address1,
                onValueChange = { address1 = it },
                label = { Text("Address Line 1") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = address2,
                onValueChange = { address2 = it },
                label = { Text("Address Line 2") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // State Dropdown
            ProfileDropdownField(
                value = selectedState,
                label = "State",
                options = states,
                onSelected = {
                    selectedState = it
                    selectedDistrict = ""
                    selectedMandal = ""
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // District Dropdown
            ProfileDropdownField(
                value = selectedDistrict,
                label = "District",
                options = districts,
                enabled = selectedState.isNotEmpty(),
                onSelected = {
                    selectedDistrict = it
                    selectedMandal = ""
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Mandal Dropdown
            ProfileDropdownField(
                value = selectedMandal,
                label = "Mandal",
                options = mandals,
                enabled = selectedDistrict.isNotEmpty(),
                onSelected = {
                    selectedMandal = it
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = pincode,
                onValueChange = {
                    if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                        pincode = it
                    }
                },
                label = { Text("Pincode") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // ------------------------------------------------
        // 3. CROP CULTIVATION
        // ------------------------------------------------
        CropCultivationSection(
            selectedCrops = selectedCrops,
            onSelectionChanged = { selectedCrops = it }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                onSave(
                    firstName.trim(),
                    lastName.trim(),
                    phone.trim(),
                    email.trim(),
                    postalAddress.trim(),
                    address1.trim(),
                    address2.trim(),
                    selectedState.trim(),
                    selectedDistrict.trim(),
                    selectedMandal.trim(),
                    pincode.trim(),
                    selectedCrops
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Update Profile", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDropdownField(
    value: String,
    label: String,
    options: List<String>,
    enabled: Boolean = true,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

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
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2E7D32),
                focusedLabelColor = Color(0xFF2E7D32)
            ),
            singleLine = true
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
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
fun ProfileSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 6.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                content = content
            )
        }
    }
}

@Composable
fun CropCultivationSection(
    selectedCrops: List<String>,
    onSelectionChanged: (List<String>) -> Unit
) {
    val availableCrops = listOf(
        "🌾 Paddy",
        "🌽 Maize",
        "🌶️ Chilli",
        "🥜 Groundnut",
        "🍅 Tomato",
        "🫘 Pulses",
        "🍌 Banana",
        "🥭 Mango",
        "🌾 Cotton"
    )

    Text(
        text = "Crop Cultivation",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(8.dp))

    @OptIn(ExperimentalLayoutApi::class)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                availableCrops.forEach { crop ->
                    val selected = selectedCrops.contains(crop)

                    FilterChip(
                        selected = selected,
                        onClick = {
                            val updateCrops = if (selected) {
                                selectedCrops - crop
                            } else {
                                selectedCrops + crop
                            }
                            onSelectionChanged(updateCrops)
                        },
                        label = { Text(text = crop, fontSize = 14.sp) },
                        shape = RoundedCornerShape(50),
                        leadingIcon = if (selected) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
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
}
