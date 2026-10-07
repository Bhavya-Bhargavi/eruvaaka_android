package com.example.myapplication.ui


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.myapplication.Model.Request.EruvaakaMenu
import com.example.myapplication.Model.Response.CreateOrderResponse
import com.example.myapplication.R
import com.example.myapplication.ViewModel.LoginViewModel
import com.example.myapplication.ViewModel.ProfileViewModel
import com.example.myapplication.ViewModel.paymentViewModel
import com.example.myapplication.Model.Request.UpdateProfileRequest
import com.example.myapplication.Utils.UpdateProfileState
import kotlinx.coroutines.launch
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.ContactMail
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.Model.Response.BookItem
import com.example.myapplication.Model.Response.ForumItem
import com.example.myapplication.Utils.ContactState
import com.example.myapplication.Utils.MagazineStorage
import com.example.myapplication.Utils.Screen
import com.example.myapplication.Utils.UserPreferences
import com.example.myapplication.ViewModel.BookViewModel
import com.example.myapplication.ViewModel.ForumViewModel
import com.example.myapplication.ViewModel.MediaViewModel
import kotlinx.coroutines.flow.first

enum class MenuType {
    HOME,
    MAGAZINE,
    SUBSCRIPTION,
    CONTACT_US,
    CATEGORY
}

val staticMenus = listOf(
    "Home",
    "Magazine",
    "Subscription",
    "Contact Us",
    "Profile",
    "Books",
    "Forums"
)

enum class ScreenType {
    HOME,
    SUBSCRIPTION,
    CONTACTUS,PROFILE,PDF_VIEWER,E_PAPER_VIEWER,NEWS,BOOKS,FORUMS
}

val gridMenuItems = listOf(
    EruvaakaMenu(
        title = "Magazine",
        icon = Icons.Default.MenuBook,
        backgroundColor = Color(0xFFE8EAF6)
    ),
    EruvaakaMenu(
        title = "E-Paper",
        icon = Icons.Default.Newspaper,
        backgroundColor = Color(0xFFE8F5E9)
    ),
    EruvaakaMenu(
        title = "Books",
        icon = Icons.Default.LibraryBooks,
        backgroundColor = Color(0xFFFCE4EC)
    ),
    EruvaakaMenu(
        title = "News",
        icon = Icons.Default.Article,
        backgroundColor = Color(0xFFE0F2F1)
    ),
    EruvaakaMenu(
        title = "Forums",
        icon = Icons.Default.Forum,
        backgroundColor = Color(0xFFFFF3E0)
    )
)

val categories = listOf(
    "తాజా వార్తలు",
    "తెలంగాణ",
    "ఆంధ్రప్రదేశ్",
    "పంటలు",
    "మార్కెట్",
    "పథకాలు"
)

/*val categories = listOf(
    "Latest",
    "Telangana",
    "Andhra Pradesh",
    "Crops",
    "Market",
    "Schemes"
)*/

// Combine menus
val menuItems = staticMenus

class MainActivity : ComponentActivity(), PaymentResultWithDataListener {
    private lateinit var pPaymentViewModel: paymentViewModel
    private lateinit var pProfileViewModel: ProfileViewModel
    private lateinit var userPreferences: UserPreferences
    private lateinit var loginViewModel: LoginViewModel
    private val mediaViewModel: MediaViewModel by viewModels()
    private val bookViewModel: BookViewModel by viewModels()
    private val forumViewModel: ForumViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Checkout.preload(applicationContext)
        pPaymentViewModel = ViewModelProvider(this)[paymentViewModel::class.java]
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]


        pProfileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        pProfileViewModel.getProfileFromApi()

        enableEdgeToEdge()
        //observePaymentState()

        setContent {
            userPreferences = UserPreferences(applicationContext)

            DashboardUI()

            /*val profile by pProfileViewModel.profile.collectAsState()

            if (profile.crop_interests.isEmpty()) {
                // Show "Update Profile" dialog
            }*/
            /*RegistrationScreen(
                onRegisterClick = { registrationData ->

                    // Here you receive all registration information

                    Log.d(
                        "Registration",
                        "User: ${registrationData.firstName}"
                    )

                    Log.d(
                        "Registration",
                        "Mobile: ${registrationData.mobile}"
                    )

                    // Call ViewModel here
                    // viewModel.register(registrationData)
                }
            )*/
            /*DemoSampleAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }*/
        }
    }

    override fun onPaymentSuccess(
        razorpayPaymentId: String?,
        paymentData: PaymentData?
    ) {
        val orderId = paymentData?.orderId
        val signature = paymentData?.signature

        if (
            razorpayPaymentId.isNullOrBlank() ||
            orderId.isNullOrBlank() ||
            signature.isNullOrBlank()
        ) {
            Toast.makeText(
                this,
                "Invalid payment response",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        pPaymentViewModel.verifyPayment(
            razorpayOrderId = orderId,
            razorpayPaymentId = razorpayPaymentId,
            razorpaySignature = signature
        )
    }

    override fun onPaymentError(
        code: Int,
        response: String?,
        paymentData: PaymentData?
    ) {
        Toast.makeText(
            this,
            "Payment failed or cancelled",
            Toast.LENGTH_LONG
        ).show()
    }

    /*override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        TODO("Not yet implemented")
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        TODO("Not yet implemented")
    }*/

    private fun openRazorpayCheckout(order: CreateOrderResponse) {
        val checkout = Checkout()

        checkout.setKeyID(order.payment_key)

        val options = JSONObject().apply {
            put("name", "Eruvaaka")
            put("description", "Magazine Subscription")
            put("currency", order.order.currency)
            put("amount", order.order.amount)
            put("order_id", order.order_id)

            put(
                "prefill",
                JSONObject().apply {
                   // put("email", "user@example.com") //user data replace
                    put("contact", "8790849906") //need to dynamic
                }
            )

            put(
                "theme",
                JSONObject().apply {
                    put("color", "#2E7D32")
                }
            )
        }

        try {
            checkout.open(this, options)
        } catch (exception: Exception) {
            Toast.makeText(
                this,
                "Unable to open payment gateway",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun observePaymentState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    pPaymentViewModel.orderResponse.collect { order ->
                        order?.let {
                            openRazorpayCheckout(it)
                        }
                    }
                }

                launch {
                    pPaymentViewModel.paymentVerification.collect { result ->
                        result?.let {
                            Toast.makeText(
                                this@MainActivity,
                                it.message,
                                Toast.LENGTH_LONG
                            ).show()

                            if (it.success) {
                                lifecycleScope.launch {
                                    Log.e("subscribe", "true")
                                    userPreferences.setSubscribed(true)
                                }
                           }
                        }
                    }
                }
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DashboardUI() {
        val isSubscribed by userPreferences.isSubscribed
            .collectAsState(initial = false)
        val context = LocalContext.current
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        var selectedItem by remember { mutableStateOf("Home") }
        var isSubscrptnClick by remember { mutableStateOf(false) }
        var currentScreen by remember { mutableStateOf(ScreenType.HOME) }

        LaunchedEffect(currentScreen) {
            selectedItem = when (currentScreen) {
                ScreenType.HOME -> "Home"
                ScreenType.CONTACTUS -> "Contact Us"
                ScreenType.PROFILE -> "Profile"
                ScreenType.SUBSCRIPTION -> "Subscription"
                ScreenType.BOOKS -> "Books"
                ScreenType.NEWS -> "News"
                ScreenType.FORUMS -> "Forums"
                ScreenType.PDF_VIEWER -> "Magazine"
                ScreenType.E_PAPER_VIEWER -> "E-Paper"
            }
        }

        BackHandler(enabled = currentScreen != ScreenType.HOME) {
            currentScreen = ScreenType.HOME
        }

        val profile by pProfileViewModel.profile.collectAsState()
        val downloadState by mediaViewModel.downloadState.collectAsState()
        var showProfileDialog by rememberSaveable {
            mutableStateOf(false)
        }

        LaunchedEffect(downloadState) {
            val res = downloadState
            if (res != null) {
                if (res.isSuccess) {
                    Toast.makeText(context, "Download Successful", Toast.LENGTH_SHORT).show()
                    // Re-check files and navigate. Since we just have 2, let's just trigger recompose or navigate
                    val storage = MagazineStorage(context)
                    val isMagValid = storage.isMagazineDownloaded("eru_vaaka_latest.pdf")
                    val isEpaperValid = storage.isMagazineDownloaded("epaper.pdf")
                    
                    if (selectedItem == "Magazine" && isMagValid) {
                        currentScreen = ScreenType.PDF_VIEWER
                    } else if (isEpaperValid) {
                        currentScreen = ScreenType.E_PAPER_VIEWER
                    }
                } else if (res.isFailure) {
                    Toast.makeText(context, "Download Failed: ${res.exceptionOrNull()?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        LaunchedEffect(profile) {

            if (profile.crop_interests.isEmpty()) {
                showProfileDialog = true
            }
        }

        if (showProfileDialog) {

            AlertDialog(
                onDismissRequest = {
                    showProfileDialog = false
                },

                title = {
                    Text("Complete Your Profile")
                },

                text = {
                    Text(
                        "Please select the crops you cultivate " +
                                "to get relevant agricultural information."
                    )
                },

                confirmButton = {

                    TextButton(
                        onClick = {
                            showProfileDialog = false
                            currentScreen = ScreenType.PROFILE

                            // Navigate to profile
                            // navController.navigate("profile")
                        }
                    ) {
                        Text("Update Profile")
                    }
                },

                dismissButton = {

                    TextButton(
                        onClick = {
                            showProfileDialog = false
                        }
                    ) {
                        Text("Later")
                    }
                }
            )
        }

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                    DrawerContent(
                        staticMenus = menuItems,
                        categories = categories,
                        selectedItem = selectedItem,
                        userName = profile.first_name,
                        phoneNumber = profile.phone,
                    onItemClick = { item ->

                        // Handle navigation logic
                        when (item) {
                            "Home" -> currentScreen = ScreenType.HOME
                            "Magazine" -> {
                                scope.launch {

                                    val subscribed = userPreferences.isSubscribed.first()

                                    if (subscribed) {
                                        Log.e("subscribed", "true")
                                        val file = MagazineStorage(context)
                                            .getMagazineFile("eru_vaaka_latest.pdf")

                                        if (file.exists()) {
                                            currentScreen = ScreenType.PDF_VIEWER
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Downloading Magazine...",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            val token = userPreferences.getToken() ?: ""
                                            mediaViewModel.downloadEMagazine(token = token, context = context)
                                        }
                                    } else {
                                        currentScreen = ScreenType.SUBSCRIPTION
                                    }
                                }
                            }
                            "Subscription" -> currentScreen = ScreenType.SUBSCRIPTION
                            "Contact Us" -> currentScreen = ScreenType.CONTACTUS
                            "Profile" -> currentScreen = ScreenType.PROFILE
                            "Books" -> currentScreen = ScreenType.BOOKS
                            "Forums" -> currentScreen = ScreenType.FORUMS
                            "E-Paper" -> {
                                val file = MagazineStorage(context)
                                    .getMagazineFile("epaper.pdf")

                                if (file.exists() && file.length() > 0) {
                                    currentScreen = ScreenType.E_PAPER_VIEWER
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Downloading E-Paper...",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    scope.launch {
                                        val token = userPreferences.getToken() ?: ""
                                        mediaViewModel.downloadEPaper(token = token, context = context)
                                    }
                                }
                            }
                            "News" -> currentScreen = ScreenType.NEWS
                            else -> {
                                // category clicked
                                currentScreen = ScreenType.NEWS
                            }
                        }

                        scope.launch { drawerState.close() }
                    }
                )
            }
        )  {

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Eruvaaka") },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch { drawerState.open() }
                            }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        },
                        actions = {
                            IconButton(onClick = { }) {
                                Icon(Icons.Default.Search, contentDescription = "Search")
                            }
                        }
                    )
                },

                bottomBar = {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    ) {
                        Button(
                            onClick = { },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF25D366), // ✅ green
                                contentColor = Color.White          // text color
                            )
                        ) {
                            Text("ACCESS E-MAGAZINE")
                        }
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp, end = 16.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "crafted by ",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "A&B Innovations",
                                fontSize = 12.sp,
                                color = Color(0xFF2E7D32),
                                textDecoration = TextDecoration.Underline,
                                modifier = Modifier.clickable {
                                    val url = "https://bhavya-bhargavi.github.io/AB-BRANDING/"
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    context.startActivity(intent)
                                }
                            )
                        }
                    }
                },



                floatingActionButton = {

                    FloatingActionButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse("https://wa.me/919999999999")
                            context.startActivity(intent)
                        },
                        containerColor = Color(0xFF25D366) // WhatsApp green
                    ) {
                        Icon(
                            painter = rememberBitmapPainter(R.drawable.whatsapp),
                            contentDescription = "WhatsApp",
                            tint = Color.White, modifier = Modifier.size(24.dp)
                        )
                    }
                    /* FloatingActionButton(onClick = {  val intent = Intent(Intent.ACTION_VIEW)
                         intent.data = Uri.parse("https://wa.me/919999999999")
                         context.startActivity(intent)}) {
                         Icon(
                             painter = painterResource(R.drawable.whatsapp),
                             contentDescription = "WhatsApp"
                         )
                     }*/
                }

            ) { padding ->

                Column(modifier = Modifier.padding(padding)) {

                    when (currentScreen) {

                        ScreenType.HOME -> {
                            EruvaakaMenuGrid(
                                onMenuClick = { menu ->

                                    when (menu) {

                                        "Magazine" -> {
                                            // Navigate to Magazine
                                            scope.launch {

                                                val subscribed =
                                                    userPreferences.isSubscribed.first()

                                                if (subscribed) {
                                                    val storage = MagazineStorage(context)
                                                    if (storage.isMagazineDownloaded("eru_vaaka_latest.pdf")) {
                                                        currentScreen = ScreenType.PDF_VIEWER
                                                    } else {
                                                        Toast.makeText(
                                                            context,
                                                            "Downloading Magazine...",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        val token = userPreferences.getToken() ?: ""
                                                        mediaViewModel.downloadEMagazine(token = token, context = context)
                                                    }
                                                } else {
                                                    currentScreen = ScreenType.SUBSCRIPTION
                                                }
                                            }

                                        }

                                        "E-Paper" -> {
                                            val storage = MagazineStorage(context)
                                            if (storage.isMagazineDownloaded("epaper.pdf")) {
                                                currentScreen = ScreenType.E_PAPER_VIEWER
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Downloading E-Paper...",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                scope.launch {
                                                    val token = userPreferences.getToken() ?: ""
                                                    mediaViewModel.downloadEPaper(token = token, context = context)
                                                }
                                            }

                                        }

                                        "Books" -> {
                                            currentScreen = ScreenType.BOOKS
                                        }

                                        "News" -> {
                                            currentScreen = ScreenType.NEWS
                                        }

                                        "Forums" -> {
                                            currentScreen = ScreenType.FORUMS
                                        }
                                    }
                                }
                            )
                        }

                        ScreenType.SUBSCRIPTION -> {
                            //SubscriptionScreen()

                            SubscriptionScreen(
                                onSubscribe = { magazineType, plan ->

                                    Log.d(
                                        "Subscription",
                                        "Type=$magazineType, Years=${plan.years}, Price=${plan.price}"
                                    )

                                    // TEMPORARY TEST ONLY
                                    lifecycleScope.launch {
                                        userPreferences.setSubscribed(true)
                                    }

                                     pPaymentViewModel.createOrder("yearly")
                                    // Your Razorpay flow
                                    //
                                    // paymentViewModel.createOrder(
                                    //     planId = ...
                                    // )
                                }
                            )

                            /*SubscriptionScreen(
                                onSubscribeClick = { planId ->
                                    pPaymentViewModel.createOrder("yearly")
                                }
                            )*/
                            /*SubscriptionScreen(
                                onSubscribeClick = { planId ->
                                    //createRazorpayOrder(planId)
                                }
                            )*/
                        }
                        ScreenType.CONTACTUS -> {
                            val contactState by loginViewModel.contactState.collectAsState()

                            ContactUsScreen(
                                isLoading = contactState is ContactState.Loading,
                                onSubmit = { name, number, email, subject, message ->
                                    loginViewModel.submitContact(name, number, email, subject, message)
                                }
                            )

                            when (val state = contactState) {
                                ContactState.Idle -> Unit
                                ContactState.Loading -> Unit
                                is ContactState.Success -> {
                                    LaunchedEffect(state) {
                                        Toast.makeText(
                                            context,
                                            state.response.message ?: "Message sent successfully!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        currentScreen = ScreenType.HOME
                                        selectedItem = "Home"
                                    }
                                }
                                is ContactState.Error -> {
                                    LaunchedEffect(state) {
                                        Toast.makeText(
                                            context,
                                            state.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                        }

                        ScreenType.PROFILE -> {
                            val updateState by pProfileViewModel.updateProfileState.collectAsState()

                            ProfileScreen(
                                viewModel = pProfileViewModel,
                                onSave = { firstName, lastName, state, district, mandal, pincode, cropInterests ->
                                    val request = UpdateProfileRequest(
                                        firstName = firstName,
                                        lastName = lastName,
                                        state = state,
                                        district = district,
                                        mandal = mandal,
                                        pincode = pincode,
                                        crop_interests = cropInterests
                                    )
                                    pProfileViewModel.updateProfile(request)
                                }
                            )

                            when (val state = updateState) {
                                UpdateProfileState.Idle -> Unit
                                UpdateProfileState.Loading -> {}
                                is UpdateProfileState.Success -> {
                                    LaunchedEffect(state) {
                                        Toast.makeText(
                                            context,
                                            state.profile.message ?: "Profile updated successfully!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        currentScreen = ScreenType.HOME
                                        selectedItem = "Home"
                                    }
                                }
                                is UpdateProfileState.Error -> {
                                    LaunchedEffect(state) {
                                        Toast.makeText(
                                            context,
                                            state.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                        }
                        ScreenType.PDF_VIEWER -> {
                            val storage = MagazineStorage(context)
                            val file = storage.getMagazineFile("eru_vaaka_latest.pdf")

                            if (storage.isValidDocument(file)) {
                                PdfViewerScreen(file = file)
                            } else {
                                Text("Magazine file not found or invalid.")
                            }
                        }
                        ScreenType.E_PAPER_VIEWER -> {
                            val storage = MagazineStorage(context)
                            val file = storage.getMagazineFile("epaper.pdf")

                            if (storage.isValidDocument(file)) {
                                PdfViewerScreen(file = file)
                            } else {
                                Text("E-Paper file not found or invalid.")
                            }
                        }
                        ScreenType.NEWS -> {
                            var selectedTab by remember { mutableStateOf(0) }
                            TabsSection(selectedTab = selectedTab, onTabSelected = { selectedTab = it })
                            NewsFeed(selectedTab = selectedTab)
                        }
                        ScreenType.BOOKS -> {
                            BooksScreen(bookViewModel)
                        }
                        ScreenType.FORUMS -> {
                            ForumsScreen(forumViewModel)
                        }
                    }
                }
            }

        }

        observePaymentState()
    }
}


@Composable
fun DrawerContent(
    staticMenus: List<String>,
    categories: List<String>,
    selectedItem: String,
    userName: String,
    phoneNumber: String,
    onItemClick: (String) -> Unit
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(310.dp)
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {

        // -----------------------------
        // USER NAME + PHONE
        // -----------------------------

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 40.dp,
                    bottom = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = userName.ifBlank { "User" },
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = phoneNumber,
                fontSize = 16.sp,
                color = Color.DarkGray
            )
        }


        // -----------------------------
        // SOCIAL MEDIA
        // -----------------------------

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            shape = RoundedCornerShape(
                bottomStart = 18.dp,
                bottomEnd = 18.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 8.dp,
                        vertical = 12.dp
                    ),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                SocialIcon(
                    icon = R.drawable.facebook,
                    contentDescription = "Facebook"
                ) {
                    openUrl(
                        context,
                        "https://www.facebook.com/"
                    )
                }

                SocialIcon(
                    icon = R.drawable.youtube,
                    contentDescription = "YouTube"
                ) {
                    openUrl(
                        context,
                        "https://www.youtube.com/"
                    )
                }

                SocialIcon(
                    icon = R.drawable.twitter,
                    contentDescription = "Twitter"
                ) {
                    openUrl(
                        context,
                        "https://twitter.com/"
                    )
                }

                SocialIcon(
                    icon = R.drawable.instagram,
                    contentDescription = "Instagram"
                ) {
                    openUrl(
                        context,
                        "https://www.instagram.com/"
                    )
                }

                /*SocialIcon(
                    icon = R.drawable.telegram,
                    contentDescription = "Telegram"
                ) {
                    openUrl(
                        context,
                        "https://t.me/"
                    )
                }*/
            }
        }


        Spacer(modifier = Modifier.height(10.dp))


        // -----------------------------
        // YOUR OLD MENUS - KEEP THEM
        // -----------------------------

        staticMenus.forEach { item ->

            DrawerItem(
                title = item,
                isSelected = item == selectedItem
            ) {
                onItemClick(item)
            }
        }


        DrawerDivider()


        // -----------------------------
        // YOUR OLD CATEGORIES - KEEP THEM
        // -----------------------------

        Text(
            text = "Categories",
            modifier = Modifier.padding(
                start = 18.dp,
                top = 4.dp,
                bottom = 4.dp
            ),
            color = Color.Gray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        categories.forEach { item ->

            DrawerItem(
                title = item,
                isSelected = item == selectedItem
            ) {
                onItemClick(item)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
@Composable
fun DrawerDivider() {
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        thickness = 1.dp
    )
}


@Composable
fun rememberBitmapPainter(@DrawableRes id: Int): Painter {
    val context = LocalContext.current
    return remember(id) {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeResource(context.resources, id, options)
        
        var sampleSize = 1
        val maxDim = 256
        val width = if (options.outWidth > 0) options.outWidth else maxDim
        val height = if (options.outHeight > 0) options.outHeight else maxDim
        
        while ((width / sampleSize) > maxDim || (height / sampleSize) > maxDim) {
            sampleSize *= 2
        }
        
        val decodeOptions = BitmapFactory.Options().apply {
            inSampleSize = sampleSize
        }
        val bitmap = try {
            BitmapFactory.decodeResource(context.resources, id, decodeOptions)
        } catch (e: Exception) {
            null
        }
        
        if (bitmap != null) {
            BitmapPainter(bitmap.asImageBitmap())
        } else {
            BitmapPainter(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888).asImageBitmap())
        }
    }
}

@Composable
fun SocialIcon(
    @DrawableRes icon: Int,
    contentDescription: String,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(52.dp)
    ) {
        Image(
            painter = rememberBitmapPainter(icon),
            contentDescription = contentDescription,
            modifier = Modifier.size(42.dp)
        )
    }
}

@Composable
fun DrawerItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isSelected)
                    Color(0xFFE8F5E9)
                else
                    Color.Transparent
            )
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 18.dp,
                vertical = 10.dp
            ),

        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = getMenuIcon(title),
            contentDescription = title,
            modifier = Modifier.size(27.dp),
            tint = if (isSelected)
                Color(0xFF2E7D32)
            else
                Color.DarkGray
        )

        Spacer(modifier = Modifier.width(15.dp))

        Text(
            text = title,
            fontSize = 15.sp,
            color = if (isSelected)
                Color(0xFF2E7D32)
            else
                Color.Black,
            fontWeight = if (isSelected)
                FontWeight.SemiBold
            else
                FontWeight.Normal
        )
    }
}

@Composable
fun getMenuIcon(title: String): ImageVector {

    return when (title) {

        "Home" ->
            Icons.Default.Home

        "Magazine" ->
            Icons.Default.MenuBook

        "Subscription" ->
            Icons.Default.CardMembership

        "Contact Us" ->
            Icons.Default.ContactMail

        "తాజా వార్తలు" ->
            Icons.Default.Article

        "తెలంగాణ" ->
            Icons.Default.LocationOn

        "ఆంధ్రప్రదేశ్" ->
            Icons.Default.LocationOn

        "పంటలు" ->
            Icons.Default.Agriculture

        "మార్కెట్" ->
            Icons.Default.TrendingUp

        "పథకాలు" ->
            Icons.Default.AccountBalance

        else ->
            Icons.Default.Menu
    }
}

/*@Composable
fun DrawerContent(
    staticMenus: List<String>,
    categories: List<String>,
    selectedItem: String,
    onItemClick: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(Color.White).padding(top = 40.dp)
            .verticalScroll(rememberScrollState())
    ) {

        // 🔷 HEADER
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF25D366))
                .padding(16.dp)
        ) {
            Text("Eruvaaka", color = Color.White, fontSize = 20.sp,
                fontWeight = FontWeight.Bold)
            //Text("ashak@risecorp.com", color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 🔷 MAIN MENU SECTION
        *//*Text(
            text = "మెను",
            modifier = Modifier.padding(start = 16.dp),
            color = Color.Gray
        )*//*

        staticMenus.forEach { item ->
            DrawerItem(item, item == selectedItem) {
                onItemClick(item)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔷 CATEGORY SECTION (Separate)
        Text(
            text = "Categories",
            modifier = Modifier.padding(start = 16.dp),
            color = Color.Gray
        )

        categories.forEach { item ->
            DrawerItem(item, item == selectedItem) {
                onItemClick(item)
            }
        }
    }
}*/

/*@Composable
fun DrawerContent(
    items: List<String>,
    selectedItem: String,
    onItemClick: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(Color.White).padding(top = 40.dp)
            .verticalScroll(rememberScrollState())
    ) {

        // 🔷 HEADER
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF5B5FEF))
                .padding(16.dp)
        ) {
            Text(
                text = "Eruvaaka",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            *//*Text(
                text = "ashak@risecorp.com",
                color = Color.White,
                fontSize = 14.sp
            )*//*
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 🔷 MENU ITEMS
        items.forEach { item ->
            DrawerItem(
                title = item,
                isSelected = item == selectedItem
            ) {
                onItemClick(item)
            }
        }


    }
}*/

/*@Composable
fun DrawerItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isSelected) Color(0xFFE8EAF6) else Color.Transparent
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {

       *//* Icon(
            imageVector = getAppIconForNameMenus(title),
            contentDescription = null,
            tint = if (isSelected) Color(0xFF5B5FEF) else Color.Gray
        )*//*

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            fontSize = 14.sp,
            color = if (isSelected) Color(0xFF25D366) else Color.Black,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}*/

@Composable
fun TabsSection(selectedTab: Int, onTabSelected: (Int) -> Unit) {

    val tabs = listOf("Latest", "Telangana", "AP", "Crops", "Market", "Schemes")

    TabRow(
        selectedTabIndex = selectedTab,
        containerColor = Color.White,
        contentColor = Color(0xFF25D366) // selected indicator color
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        title,
                        color = if (selectedTab == index)
                            Color(0xFF25D366)   // ✅ selected tab text color
                        else
                            Color.Gray         // ✅ unselected color
                    )
                }
            )
        }
    }
}

@Composable
fun NewsFeed(
    mediaViewModel: MediaViewModel = viewModel(),
    selectedTab: Int
) {

    val newsState by mediaViewModel.newsState.collectAsState()
    val tabs = listOf("Latest", "Telangana", "AP", "Crops", "Market", "Schemes")

    LaunchedEffect(Unit) {
        mediaViewModel.fetchNews()
    }

    LazyColumn {
        val newsResult = newsState
        if (newsResult == null) {
            item {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
        } else if (newsResult.isSuccess) {
            val newsResponse = newsResult.getOrNull()
            if (newsResponse != null && newsResponse.items.isNotEmpty()) {
                val allItems = newsResponse.items
                
                // Filter items based on the selected tab
                val filteredItems = when (tabs[selectedTab]) {
                    "Latest" -> allItems
                    "Telangana" -> allItems.filter { item ->
                        item.categories.any { cat -> cat.contains("తెలంగాణ", ignoreCase = true) || cat.contains("Telangana", ignoreCase = true) }
                    }
                    "AP" -> allItems.filter { item ->
                        item.categories.any { cat -> cat.contains("ఆంధ్రప్రదేశ్", ignoreCase = true) || cat.contains("Andhra", ignoreCase = true) }
                    }
                    "Crops" -> allItems.filter { item ->
                        item.categories.any { cat -> cat.contains("వ్యవసాయ పంటలు", ignoreCase = true) || cat.contains("పంటలు", ignoreCase = true) || cat.contains("Crop", ignoreCase = true) }
                    }
                    "Market" -> allItems.filter { item ->
                        item.categories.any { cat -> cat.contains("వ్యవసాయ వాణిజ్యం", ignoreCase = true) || cat.contains("వాణిజ్యం", ignoreCase = true) || cat.contains("Market", ignoreCase = true) || cat.contains("Trade", ignoreCase = true) }
                    }
                    "Schemes" -> allItems.filter { item ->
                        item.categories.any { cat -> cat.contains("పథకాలు", ignoreCase = true) || cat.contains("Scheme", ignoreCase = true) }
                    }
                    else -> allItems
                }

                if (filteredItems.isNotEmpty()) {
                    items(filteredItems.size) { index ->
                        val item = filteredItems[index]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = item.title,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = item.published_at,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = Html.fromHtml(item.description, Html.FROM_HTML_MODE_COMPACT).toString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.DarkGray
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "By ${item.creator}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                } else {
                    item {
                        Text(
                            text = "No news available for ${tabs[selectedTab]}.",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                item {
                    Text(
                        text = "No news available at the moment.",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {
            item {
                Text(
                    text = "Failed to load news: ${newsResult.exceptionOrNull()?.message}",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Red
                )
            }
        }
    }
}

@Composable
fun EruvaakaMenuGrid(
    onMenuClick: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {

        Text(
            text = "Explore Eruvaaka",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF00695C),
            modifier = Modifier.padding(
                start = 12.dp,
                bottom = 12.dp
            )
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth().background(color = Color.White)
                    .padding(10.dp)
            ) {

                // First row - 3 items
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    MenuCard(
                        item = gridMenuItems[0],
                        modifier = Modifier.weight(1f),
                        onClick = onMenuClick
                    )

                    MenuCard(
                        item = gridMenuItems[1],
                        modifier = Modifier.weight(1f),
                        onClick = onMenuClick
                    )

                    MenuCard(
                        item = gridMenuItems[2],
                        modifier = Modifier.weight(1f),
                        onClick = onMenuClick
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Second row - 2 centered items
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    MenuCard(
                        item = gridMenuItems[3],
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp),
                        onClick = onMenuClick
                    )

                    MenuCard(
                        item = gridMenuItems[4],
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp),
                        onClick = onMenuClick
                    )

                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun MenuCard(
    item: EruvaakaMenu,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {

    Card(
        modifier = modifier
            .height(125.dp)
            .clickable {
                onClick(item.title)
            },

        shape = RoundedCornerShape(2.dp),

        colors = CardDefaults.cardColors(
            containerColor = item.backgroundColor
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),

            horizontalAlignment = Alignment.CenterHorizontally,

            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                imageVector = item.icon,
                contentDescription = item.title,

                modifier = Modifier.size(48.dp),

                tint = Color.Black
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = item.title,

                fontSize = 16.sp,

                fontWeight = FontWeight.Medium,

                color = Color.Black,

                textAlign = TextAlign.Center
            )
        }
    }
}



/*val context = LocalContext.current

FloatingActionButton(onClick = {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse("https://wa.me/919999999999")
    context.startActivity(intent)
}) {
    Text("WA")
}*/

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}

fun openUrl(context: Context, url: String) {
    try {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(
            context,
            "Unable to open link",
            Toast.LENGTH_SHORT
        ).show()
    }
}

@Composable
fun BooksScreen(bookViewModel: BookViewModel) {
    val booksState by bookViewModel.booksState.collectAsState()

    LaunchedEffect(Unit) {
        bookViewModel.fetchBooks()
    }

    // Default sample list from user provided books if backend data is empty
    val defaultBooks = listOf(
        BookItem(
            id = 1,
            title = "Different Winter",
            author = "Mia Jackson",
            description = "A gripping literature book exploring winter themes and personal journeys.",
            cover_image = ""
        ),
        BookItem(
            id = 2,
            title = "Wise Steps For Success",
            author = "Motivational Series",
            description = "Essential guide and wise steps towards personal and professional success.",
            cover_image = ""
        ),
        BookItem(
            id = 3,
            title = "English Project",
            author = "Educational Resources",
            description = "Comprehensive English project guide with creative notes and templates.",
            cover_image = ""
        ),
        BookItem(
            id = 4,
            title = "English Notebook",
            author = "Eruvaaka Learning",
            description = "Handcrafted notes and calligraphy practice material for English learning.",
            cover_image = ""
        ),
        BookItem(
            id = 5,
            title = "Modern History",
            author = "Sirat Kaur",
            description = "Class 8th Modern History illustrated guide and study reference.",
            cover_image = ""
        )
    )

    val result = booksState
    val bookList = if (result?.isSuccess == true) {
        val apiData = result.getOrNull()?.data
        if (!apiData.isNullOrEmpty()) apiData else defaultBooks
    } else {
        defaultBooks
    }

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Text(
            text = "E-Books & Publications",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            color = Color(0xFF1B5E20)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(bookList.size) { index ->
                val book = bookList[index]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp, horizontal = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Placeholder cover icon/box
                        Box(
                            modifier = Modifier
                                .size(width = 60.dp, height = 80.dp)
                                .background(
                                    color = Color(0xFFE8F5E9),
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = "Book Cover",
                                tint = Color(0xFF25D366),
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = book.title ?: "Untitled Book",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                            if (!book.author.isNullOrBlank()) {
                                Text(
                                    text = "By ${book.author}",
                                    fontSize = 13.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                            if (!book.description.isNullOrBlank()) {
                                Text(
                                    text = book.description,
                                    fontSize = 12.sp,
                                    color = Color.DarkGray,
                                    maxLines = 2,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            Button(
                                onClick = { /* Handle download / view */ },
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .height(32.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
                            ) {
                                Text(text = "Read / Download", fontSize = 11.sp, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ForumsScreen(forumViewModel: ForumViewModel) {
    val forumsState by forumViewModel.forumsState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var selectedForumForComment by remember { mutableStateOf<ForumItem?>(null) }
    var commentText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        forumViewModel.fetchForums()
    }

    val result = forumsState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            text = "Community Forums & Discussions",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            color = Color(0xFF1B5E20)
        )

        if (result == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF25D366))
            }
        } else if (result.isSuccess) {
            val forumResponse = result.getOrNull()
            val forums = forumResponse?.data ?: emptyList()

            if (forums.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(forums.size) { index ->
                        val forum = forums[index]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp, horizontal = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = forum.title ?: "Discussion Title",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = forum.body ?: "",
                                    fontSize = 14.sp,
                                    color = Color.DarkGray,
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Forum,
                                            contentDescription = "Comments",
                                            tint = Color.Gray,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "${forum.comments_count ?: 0} Comments",
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                    }

                                    TextButton(
                                        onClick = { selectedForumForComment = forum },
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Article,
                                            contentDescription = "Add Comment",
                                            tint = Color(0xFF25D366),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Add Comment",
                                            fontSize = 12.sp,
                                            color = Color(0xFF25D366),
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No forum discussions found.",
                        color = Color.Gray,
                        fontSize = 15.sp
                    )
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Failed to load forums: ${result.exceptionOrNull()?.message}",
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }

    // Comment Dialog
    selectedForumForComment?.let { forum ->
        AlertDialog(
            onDismissRequest = { selectedForumForComment = null },
            title = { Text("Comment on ${forum.title}") },
            text = {
                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    label = { Text("Write your comment...") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (commentText.isNotBlank()) {
                            // Call post comment
                            val userPrefs = UserPreferences(context)
                            scope.launch {
                                val token = userPrefs.getToken() ?: ""
                                forumViewModel.postForumComment(token, forum.id ?: 1, commentText)
                                Toast.makeText(context, "Comment submitted!", Toast.LENGTH_SHORT).show()
                                commentText = ""
                                selectedForumForComment = null
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
                ) {
                    Text("Submit")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedForumForComment = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}