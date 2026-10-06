
package com.example.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.Utils.LoginState
import com.example.myapplication.Utils.RegistrationState
import com.example.myapplication.Utils.UserPreferences
import com.example.myapplication.Utils.VerifyOtpState
import com.example.myapplication.ViewModel.LoginViewModel
import com.example.myapplication.ViewModel.RegistrationViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginAct : ComponentActivity() {

    private val registrationViewModel: RegistrationViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
     var expectedOtp: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()


        lifecycleScope.launch {

            val userPreferences = UserPreferences(this@LoginAct)

            val token = userPreferences.token.first()
            Log.e("token", token.toString())
            Log.e("tokenInstall", token.toString())

            if (!token.isNullOrBlank()) {
                Log.e(token, token.toString())
                // Token available → directly open Dashboard
                startActivity(
                    Intent(
                        this@LoginAct,
                        MainActivity::class.java
                    )
                )

                finish()

            } else {

                // Token not available → show Login
                setContent {

                    MaterialTheme {

                        val navController = rememberNavController()

                        LoginNavigation(
                            navController = navController
                        )
                    }
                }
            }
        }

        /*setContent {

            MaterialTheme {



                val navController = rememberNavController()

                LoginNavigation(
                    navController = navController
                )
            }
        }*/
    }

    @Composable
    private fun LoginNavigation(navController: NavHostController) {

        NavHost(
            navController = navController,
            startDestination = "login"
        ) {

            /*
             * --------------------------------------------
             * LOGIN
             * --------------------------------------------
             */

            composable("login") {

                val loginState by loginViewModel.loginState
                    .collectAsState()

                LoginScreen(

                    onSendOtpClick = { mobile ->

                        // Call LOGIN API
                        loginViewModel.loginUser(mobile)
                    },

                    onRegisterClick = {

                        navController.navigate(
                            "registration"
                        )
                    }
                )

                when (val state = loginState) {

                    LoginState.Idle -> Unit

                    LoginState.Loading -> {
                        // Show loading
                    }

                    is LoginState.Success -> {
                        Log.e("otp", state.otp)
                        expectedOtp = state.otp

                        Toast.makeText(this@LoginAct, state.otp, Toast.LENGTH_LONG)

                        LaunchedEffect(state) {
                            navController.navigate(
                                "otp/${state.mobile}"
                            )
                            /*navController.navigate(
                                "otp/${state.otp}"
                            )*/
                        }
                    }

                    is LoginState.Error -> {

                        Toast.makeText(
                            this@LoginAct,
                            state.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

           /* composable("login") {

                LoginScreen(

                    onSendOtpClick = { mobile ->

                        *//*
                         * IMPORTANT:
                         *
                         * In production:
                         *
                         * 1. Call API
                         * 2. Check whether mobile is registered
                         * 3. If registered:
                         *       Send OTP
                         *       Navigate to OTP
                         * 4. If not registered:
                         *       Show "Please register"
                         *
                         * For now we navigate directly
                         * so you can test the complete UI flow.
                         *//*

                        navController.navigate(
                            "otp/$mobile"
                        )
                    },

                    onRegisterClick = {

                        navController.navigate(
                            "registration"
                        )
                    }
                )
            }*/


            /*
             * --------------------------------------------
             * REGISTRATION
             * --------------------------------------------
             */

            composable("registration") {

                val registrationState by registrationViewModel.registrationState
                    .collectAsState()

                RegistrationScreen(
                    onRegisterClick = { request ->

                        registrationViewModel.registerUser(request)
                    }
                )

                when (val state = registrationState) {

                    RegistrationState.Idle -> Unit

                    RegistrationState.Loading -> {
                        // Show loading indicator
                    }

                    is RegistrationState.Success -> {

                        LaunchedEffect(state) {

                            navController.navigate(
                                "otp/${state.mobile}"
                            )
                        }
                    }

                    is RegistrationState.Error -> {

                        Toast.makeText(
                            this@LoginAct,
                            state.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            /*composable("registration") {

                RegistrationScreen(
                    onRegisterClick = { request ->

                        registrationViewModel.registerUser(request)
                    }
                )


                *//*RegistrationScreen(onRegisterClick = { registrationData ->

                        *//**//*
                         * Call registration API here.
                         *
                         * After successful registration,
                         * send OTP to the registered mobile.
                         *//**//*

                        navController.navigate(
                            "otp/${registrationData.mobile}"
                        )
                    }
                )*//*
            }*/


            /*
             * --------------------------------------------
             * OTP
             * --------------------------------------------
             */

            composable(
                route = "otp/{mobile}",
                arguments = listOf(
                    navArgument("mobile") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->

                val mobileNumber =
                    backStackEntry.arguments
                        ?.getString("mobile")
                        ?: ""

                val pVerifyOtpState by loginViewModel.verifyOtpState
                    .collectAsState()


                OtpScreen(

                    mobileNumber = mobileNumber,

                    onOtpVerified = { expectedOtp

                        //need to develop verify-otp api
                        /*
                         * OTP VALIDATION SUCCESS
                         *
                         * After backend confirms OTP,
                         * open MainActivity.
                         */

                        loginViewModel.verifyOtp(mobileNumber, expectedOtp)

                    }
                )

                when (val state = pVerifyOtpState) {

                    VerifyOtpState.Idle -> Unit

                    VerifyOtpState.Loading -> {
                        // Show loading indicator
                    }

                    is VerifyOtpState.Success -> {

                        LaunchedEffect(state) {

                            val intent =
                                Intent(
                                    this@LoginAct,
                                    MainActivity::class.java
                                )

                            startActivity(intent)

                            /*
                             * Finish LoginAct so user cannot
                             * press Back and return to login.
                             */

                            finish()
                        }
                    }

                    is VerifyOtpState.Error -> {

                        Toast.makeText(
                            this@LoginAct,
                            state.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }


    /*
     * ====================================================
     * LOGIN SCREEN
     * ====================================================
     */

    @Composable
    private fun LoginScreen(
        onSendOtpClick: (String) -> Unit,
        onRegisterClick: () -> Unit
    ) {

        var mobileNumber by rememberSaveable {
            mutableStateOf("")
        }

        val isMobileValid =
            mobileNumber.length == 10

        Scaffold(
            containerColor = Color(0xFFF6F9F4)
        ) { paddingValues ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(
                            rememberScrollState()
                        )
                        .padding(
                            horizontal = 20.dp
                        ),
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Spacer(
                        modifier = Modifier.height(55.dp)
                    )


                    /*
                     * --------------------------------
                     * AGRICULTURE LOGO
                     * --------------------------------
                     */

                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(
                                Color(0xFFE8F5E9)
                            ),
                        contentAlignment =
                            Alignment.Center
                    ) {

                        Text(
                            text = "🌱",
                            fontSize = 46.sp
                        )
                    }


                    Spacer(
                        modifier = Modifier.height(18.dp)
                    )


                    /*
                     * --------------------------------
                     * TITLE
                     * --------------------------------
                     */

                    Text(
                        text = "Welcome Back!",
                        fontSize = 26.sp,
                        fontWeight =
                            FontWeight.Bold,
                        color =
                            Color(0xFF1B5E20)
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text =
                            "Login to continue your farming journey",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign =
                            TextAlign.Center
                    )


                    Spacer(
                        modifier = Modifier.height(30.dp)
                    )


                    /*
                     * --------------------------------
                     * LOGIN CARD
                     * --------------------------------
                     */

                    Card(
                        modifier =
                            Modifier.fillMaxWidth(),
                        shape =
                            RoundedCornerShape(22.dp),
                        colors =
                            CardDefaults.cardColors(
                                containerColor =
                                    Color.White
                            ),
                        elevation =
                            CardDefaults.cardElevation(
                                defaultElevation = 2.dp
                            )
                    ) {

                        Column(
                            modifier =
                                Modifier.padding(20.dp)
                        ) {

                            Text(
                                text = "Login",
                                fontSize = 20.sp,
                                fontWeight =
                                    FontWeight.Bold,
                                color =
                                    Color(0xFF263328)
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(5.dp)
                            )

                            Text(
                                text =
                                    "Enter your registered mobile number",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(18.dp)
                            )


                            Text(
                                text = "Mobile Number",
                                fontSize = 14.sp,
                                fontWeight =
                                    FontWeight.Medium,
                                color =
                                    Color(0xFF263328)
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(8.dp)
                            )


                            /*
                             * --------------------------------
                             * MOBILE NUMBER
                             * --------------------------------
                             */

                            OutlinedTextField(

                                value =
                                    mobileNumber,

                                onValueChange = { value ->

                                    val filteredValue =
                                        value.filter {
                                            it.isDigit()
                                        }

                                    if (
                                        filteredValue.length <= 10
                                    ) {
                                        mobileNumber =
                                            filteredValue
                                    }
                                },

                                modifier =
                                    Modifier.fillMaxWidth(),

                                placeholder = {
                                    Text(
                                        "Enter mobile number"
                                    )
                                },

                                leadingIcon = {

                                    Icon(
                                        imageVector =
                                            Icons.Default.Phone,
                                        contentDescription =
                                            "Mobile number"
                                    )
                                },

                                prefix = {

                                    Text(
                                        text = "+91 ",
                                        fontWeight =
                                            FontWeight.Medium
                                    )
                                },

                                singleLine = true,

                                keyboardOptions =
                                    KeyboardOptions(
                                        keyboardType =
                                            KeyboardType.Phone
                                    ),

                                shape =
                                    RoundedCornerShape(14.dp),

                                colors =
                                    OutlinedTextFieldDefaults
                                        .colors(
                                            focusedBorderColor =
                                                Color(0xFF2E7D32),
                                            focusedLabelColor =
                                                Color(0xFF2E7D32),
                                            cursorColor =
                                                Color(0xFF2E7D32)
                                        )
                            )


                            Spacer(
                                modifier =
                                    Modifier.height(20.dp)
                            )


                            /*
                             * --------------------------------
                             * SEND OTP
                             * --------------------------------
                             */

                            Button(

                                onClick = {
                                    onSendOtpClick(mobileNumber)
                                },

                                enabled =
                                    isMobileValid,

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp),

                                shape =
                                    RoundedCornerShape(15.dp),

                                colors =
                                    ButtonDefaults
                                        .buttonColors(
                                            containerColor =
                                                Color(0xFF2E7D32),
                                            disabledContainerColor =
                                                Color(0xFFBDBDBD)
                                        )
                            ) {

                                Text(
                                    text = "Send OTP",
                                    fontSize = 16.sp,
                                    fontWeight =
                                        FontWeight.Bold
                                )
                            }


                            Spacer(
                                modifier =
                                    Modifier.height(18.dp)
                            )


                            /*
                             * --------------------------------
                             * REGISTER
                             * --------------------------------
                             */

                            Row(
                                modifier =
                                    Modifier.fillMaxWidth(),
                                horizontalArrangement =
                                    Arrangement.Center,
                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {

                                Text(
                                    text =
                                        "Don't have an account?",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )

                                TextButton(
                                    onClick =
                                        onRegisterClick
                                ) {

                                    Text(
                                        text = "Register",
                                        fontSize = 14.sp,
                                        fontWeight =
                                            FontWeight.Bold,
                                        color =
                                            Color(0xFF2E7D32)
                                    )
                                }
                            }
                        }
                    }


                    Spacer(
                        modifier =
                            Modifier.height(28.dp)
                    )


                    /*
                     * --------------------------------
                     * FOOTER
                     * --------------------------------
                     */

                    Row(
                        horizontalArrangement =
                            Arrangement.Center,
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Text(
                            text = "🌾",
                            fontSize = 18.sp
                        )

                        Spacer(
                            modifier =
                                Modifier.width(6.dp)
                        )

                        Text(
                            text =
                                "Grow smarter. Farm better.",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }


    /*
     * ====================================================
     * OTP SCREEN
     * ====================================================
     */

    @Composable
    private fun OtpScreen(
        mobileNumber: String,
        onOtpVerified: (String) -> Unit
    ) {

        var otp by rememberSaveable {
            mutableStateOf("")
        }

        val isOtpValid =
            otp.length == 6

        Scaffold(
            containerColor =
                Color(0xFFF6F9F4)
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(
                        rememberScrollState()
                    ),
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Spacer(
                    modifier =
                        Modifier.height(65.dp)
                )


                /*
                 * --------------------------------
                 * OTP ICON
                 * --------------------------------
                 */

                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(
                            Color(0xFFE8F5E9)
                        ),
                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text = "🔐",
                        fontSize = 42.sp
                    )
                }


                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )


                Text(
                    text =
                        "Verify Mobile Number",
                    fontSize = 24.sp,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        Color(0xFF1B5E20)
                )


                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )


                Text(
                    text =
                        "Enter the 6-digit OTP sent to",
                    fontSize = 14.sp,
                    color = Color.Gray
                )


                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )


                Text(
                    text =
                        "+91 $mobileNumber",
                    fontSize = 15.sp,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        Color(0xFF263328)
                )


                Spacer(
                    modifier =
                        Modifier.height(30.dp)
                )


                /*
                 * --------------------------------
                 * OTP CARD
                 * --------------------------------
                 */

                Card(
                    modifier =
                        Modifier.fillMaxWidth(),
                    shape =
                        RoundedCornerShape(22.dp),
                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                Color.White
                        ),
                    elevation =
                        CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        )
                ) {

                    Column(
                        modifier =
                            Modifier.padding(20.dp)
                    ) {

                        OutlinedTextField(

                            value = otp,

                            onValueChange = { value ->

                                val filtered =
                                    value.filter {
                                        it.isDigit()
                                    }

                                if (
                                    filtered.length <= 6
                                ) {
                                    otp = filtered
                                }
                            },

                            modifier =
                                Modifier.fillMaxWidth(),

                            placeholder = {
                                Text(
                                    "Enter OTP"
                                )
                            },

                            singleLine = true,

                            keyboardOptions =
                                KeyboardOptions(
                                    keyboardType =
                                        KeyboardType.Number
                                ),

                            shape =
                                RoundedCornerShape(14.dp),

                            colors =
                                OutlinedTextFieldDefaults
                                    .colors(
                                        focusedBorderColor =
                                            Color(0xFF2E7D32),
                                        cursorColor =
                                            Color(0xFF2E7D32)
                                    )
                        )


                        Spacer(
                            modifier =
                                Modifier.height(20.dp)
                        )


                        /*
                         * --------------------------------
                         * VERIFY OTP
                         * --------------------------------
                         */

                        Button(

                            onClick = {

                                /*
                                 * IMPORTANT:
                                 *
                                 * Replace this with:
                                 *
                                 * viewModel.verifyOtp(
                                 *     mobileNumber,
                                 *     otp
                                 * )
                                 *
                                 * Call onOtpVerified() ONLY
                                 * when API says OTP is valid.
                                 */

                                onOtpVerified(expectedOtp)
                            },

                            enabled =
                                isOtpValid,

                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),

                            shape =
                                RoundedCornerShape(15.dp),

                            colors =
                                ButtonDefaults
                                    .buttonColors(
                                        containerColor =
                                            Color(0xFF2E7D32),
                                        disabledContainerColor =
                                            Color(0xFFBDBDBD)
                                    )
                        ) {

                            Text(
                                text = "Verify OTP",
                                fontSize = 16.sp,
                                fontWeight =
                                    FontWeight.Bold
                            )
                        }


                        Spacer(
                            modifier =
                                Modifier.height(12.dp)
                        )


                        /*
                         * --------------------------------
                         * RESEND OTP
                         * --------------------------------
                         */

                        TextButton(
                            onClick = {

                                /*
                                 * Call resend OTP API
                                 */
                            },
                            modifier =
                                Modifier.fillMaxWidth()
                        ) {

                            Text(
                                text = "Resend OTP",
                                color =
                                    Color(0xFF2E7D32),
                                fontWeight =
                                    FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
