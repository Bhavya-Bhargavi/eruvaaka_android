package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
    onSave: ( firstName: String, lastName: String, state: String, district: String, mandal: String,
              pincode: String,
              cropInterests: List<String>) -> Unit
) {

    val profile by viewModel.profile.collectAsState()

    var firstName by rememberSaveable { mutableStateOf(profile.first_name.orEmpty()) }
    var lastName by rememberSaveable { mutableStateOf(profile.last_name.orEmpty()) }
    var state by rememberSaveable { mutableStateOf(profile.state.orEmpty()) }
    var district by rememberSaveable { mutableStateOf(profile.district.orEmpty()) }
    var mandal by rememberSaveable { mutableStateOf(profile.mandal.orEmpty()) }
    var pincode by rememberSaveable { mutableStateOf(profile.pincode.orEmpty()) }

   /* var selectedCrops by rememberSaveable {
        mutableStateOf(setOf<String>())
    }*/

    var selectedCrops by rememberSaveable {
        mutableStateOf(profile.crop_interests ?: emptyList())}


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
    ) {

        Text(
            text = "My Profile",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Profile header

        Text(
            text = "${profile.first_name} ${profile.last_name}",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = profile.phone,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(10.dp))

        ProfileSection(
            title = "Personal Information"
        ) {
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

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Location",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state,
                onValueChange = { state = it },
                label = { Text("State") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = district,
                onValueChange = { district = it },
                label = { Text("District") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = mandal,
                onValueChange = { mandal = it },
                label = { Text("Mandal") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
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
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            /*ProfileItem(
                label = "First Name",
                value = profile.first_name
            )

            ProfileItem(
                label = "Last Name",
                value = profile.last_name
            )

            ProfileItem(
                label = "Email",
                value = profile.email.orEmpty().ifEmpty { "Not provided" }
            )

            ProfileItem(
                label = "Mobile",
                value = profile.phone
            )*/
        }


        Spacer(modifier = Modifier.height(20.dp))

        CropCultivationSection(selectedCrops,
            onSelectionChanged = {
                selectedCrops = it
            }
        )

        Button(
            onClick =  {onSave(
                firstName.trim(),
                lastName.trim(),
                state.trim(),
                district.trim(),
                mandal.trim(),
                pincode.trim(),
                selectedCrops
            )},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update Profile")
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
            .fillMaxWidth().background(Color.White)
            .padding(vertical = 10.dp)
    ) {

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth().background(Color.White)
        ) {

            Column(
                modifier = Modifier.padding(16.dp),
                content = content
            )
        }
    }
}

@Composable
fun ProfileItem(
    label: String,
    value: String
) {

    Column(
        modifier = Modifier.padding(vertical = 6.dp)
    ) {

        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun CropCultivationSection(selectedCrops: List<String>, onSelectionChanged: (List<String>) -> Unit
) {

    val availableCrops = listOf(

        "🌾 Paddy",
        "🌽 Maize",
        "🌶️ Chilli",
        "🥜 Groundnut",
        "🍅 Tomato",
        "🫘 Pulses",
        "🍌 Banana",
        "🥭 Mango", " 🌾Cotton"

    )

    Text(
        text = "Crop Cultivation",
        style = MaterialTheme.typography.titleMedium
    )

    Spacer(modifier = Modifier.height(8.dp))

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

                availableCrops.forEach { crop ->

                    val selected =
                        selectedCrops.contains(crop)

                    FilterChip(
                        selected = selected,
                        onClick = {

                           val updateCrops =
                                if (selected) {
                                    selectedCrops - crop
                                } else {
                                    selectedCrops + crop
                                }
                            onSelectionChanged(updateCrops)
                        }
                        ,
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








    /*Column {

        Text(
            text = "Crop Cultivation",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        availableCrops.forEach { crop ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {

                        val updatedCrops =
                            if (selectedCrops.contains(crop)) {
                                selectedCrops - crop
                            } else {
                                selectedCrops + crop
                            }

                        onSelectionChanged(updatedCrops)
                    }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Checkbox(
                    checked = selectedCrops.contains(crop),
                    onCheckedChange = {

                        val updatedCrops =
                            if (selectedCrops.contains(crop)) {
                                selectedCrops - crop
                            } else {
                                selectedCrops + crop
                            }

                        onSelectionChanged(updatedCrops)
                    }
                )

                Text(
                    text = crop,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }*/
}
