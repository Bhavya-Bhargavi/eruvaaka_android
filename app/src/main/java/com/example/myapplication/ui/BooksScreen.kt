package com.example.myapplication.ui



import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ----------------------------------------------------
// MODELS
// ----------------------------------------------------

data class AgricultureBook(
    val id: Int,
    val title: String,
    val author: String,
    val category: String,
    val description: String,
    val price: Int,
    val emoji: String
)

data class BookCategory(
    val name: String,
    val subtitle: String,
    val emoji: String,
    val books: List<AgricultureBook>
)

// ----------------------------------------------------
// SAMPLE AGRICULTURE BOOK DATA
// Replace this with your API response later.
// ----------------------------------------------------

private val agricultureCategories = listOf(

    BookCategory(
        name = "Crop Cultivation",
        subtitle = "Learn better farming techniques",
        emoji = "🌾",
        books = listOf(
            AgricultureBook(
                1,
                "Modern Paddy Cultivation",
                "Agriculture Experts",
                "Crop Cultivation",
                "Learn paddy cultivation methods, nursery preparation, " +
                        "transplanting, irrigation, and harvesting.",
                199,
                "🌾"
            ),
            AgricultureBook(
                2,
                "Cotton Farming Guide",
                "Agriculture Experts",
                "Crop Cultivation",
                "A practical guide to cotton varieties, soil preparation, " +
                        "fertilizer management, and harvesting.",
                249,
                "🌱"
            ),
            AgricultureBook(
                3,
                "Chilli Cultivation",
                "Agriculture Experts",
                "Crop Cultivation",
                "Understand chilli varieties, nursery management, " +
                        "nutrient requirements, and crop protection.",
                179,
                "🌶️"
            ),
            AgricultureBook(
                4,
                "Maize Production",
                "Agriculture Experts",
                "Crop Cultivation",
                "Explore maize cultivation, irrigation, nutrient management, " +
                        "and improved production techniques.",
                199,
                "🌽"
            ),
            AgricultureBook(
                5,
                "Groundnut Farming",
                "Agriculture Experts",
                "Crop Cultivation",
                "Learn groundnut cultivation, seed selection, " +
                        "soil preparation, and harvesting.",
                189,
                "🥜"
            ),
            AgricultureBook(
                6,
                "Vegetable Cultivation",
                "Agriculture Experts",
                "Crop Cultivation",
                "A guide to growing vegetables using practical " +
                        "cultivation and crop management methods.",
                229,
                "🥬"
            )
        )
    ),

    BookCategory(
        name = "Pest & Disease Management",
        subtitle = "Protect crops and improve yield",
        emoji = "🐛",
        books = listOf(
            AgricultureBook(
                7,
                "Common Crop Pests",
                "Agriculture Experts",
                "Pest & Disease Management",
                "Identify common crop pests and learn integrated " +
                        "approaches to pest management.",
                199,
                "🐛"
            ),
            AgricultureBook(
                8,
                "Plant Disease Guide",
                "Agriculture Experts",
                "Pest & Disease Management",
                "Learn about common plant diseases, symptoms, " +
                        "prevention, and crop health.",
                249,
                "🍃"
            ),
            AgricultureBook(
                9,
                "Organic Pest Control",
                "Agriculture Experts",
                "Pest & Disease Management",
                "Discover preventive crop protection methods " +
                        "and biological pest management.",
                179,
                "🌿"
            ),
            AgricultureBook(
                10,
                "Integrated Pest Management",
                "Agriculture Experts",
                "Pest & Disease Management",
                "Understand integrated pest management and " +
                        "responsible crop protection practices.",
                299,
                "🔬"
            ),
            AgricultureBook(
                11,
                "Crop Disease Prevention",
                "Agriculture Experts",
                "Pest & Disease Management",
                "Learn crop monitoring and preventive practices " +
                        "for healthier plants.",
                199,
                "🌱"
            )
        )
    ),

    BookCategory(
        name = "Organic & Natural Farming",
        subtitle = "Sustainable farming practices",
        emoji = "🌿",
        books = listOf(
            AgricultureBook(
                12,
                "Organic Farming Basics",
                "Agriculture Experts",
                "Organic & Natural Farming",
                "An introduction to organic agriculture, " +
                        "soil health, and sustainable crop production.",
                249,
                "🌿"
            ),
            AgricultureBook(
                13,
                "Natural Fertilizers",
                "Agriculture Experts",
                "Organic & Natural Farming",
                "Learn about organic nutrient sources, " +
                        "compost, and soil fertility.",
                199,
                "🍀"
            ),
            AgricultureBook(
                14,
                "Vermicompost Production",
                "Agriculture Experts",
                "Organic & Natural Farming",
                "Understand earthworm composting, production " +
                        "methods, and compost management.",
                179,
                "🪱"
            ),
            AgricultureBook(
                15,
                "Biofertilizers for Crops",
                "Agriculture Experts",
                "Organic & Natural Farming",
                "Explore biofertilizers and their role " +
                        "in crop nutrition and soil health.",
                229,
                "🌱"
            ),
            AgricultureBook(
                16,
                "Natural Farming Methods",
                "Agriculture Experts",
                "Organic & Natural Farming",
                "An overview of natural farming practices " +
                        "and resource-efficient agriculture.",
                279,
                "🌾"
            )
        )
    ),

    BookCategory(
        name = "Agriculture & Marketing",
        subtitle = "Markets, income and modern farming",
        emoji = "💰",
        books = listOf(
            AgricultureBook(
                17,
                "Agricultural Marketing",
                "Agriculture Experts",
                "Agriculture & Marketing",
                "Learn about agricultural markets, selling " +
                        "channels, and marketing strategies.",
                249,
                "📈"
            ),
            AgricultureBook(
                18,
                "Understanding Market Prices",
                "Agriculture Experts",
                "Agriculture & Marketing",
                "Understand agricultural price trends, " +
                        "market information, and price discovery.",
                199,
                "💹"
            ),
            AgricultureBook(
                19,
                "Farm Business Management",
                "Agriculture Experts",
                "Agriculture & Marketing",
                "Learn farm budgeting, cost management, " +
                        "planning, and profitability.",
                299,
                "💰"
            ),
            AgricultureBook(
                20,
                "Government Schemes for Farmers",
                "Agriculture Experts",
                "Agriculture & Marketing",
                "An introduction to farmer support schemes, " +
                        "eligibility, and application processes.",
                149,
                "📘"
            ),
            AgricultureBook(
                21,
                "Modern Agriculture Technology",
                "Agriculture Experts",
                "Agriculture & Marketing",
                "Explore modern agricultural technologies " +
                        "and digital tools for farming.",
                299,
                "🚜"
            )
        )
    )
)

// ----------------------------------------------------
// COLORS
// ----------------------------------------------------

private val AgricultureGreen = Color(0xFF237A43)
private val AgricultureDark = Color(0xFF194F2E)
private val AgricultureBackground = Color(0xFFF6F8F4)
private val AgricultureText = Color(0xFF26342A)

// ----------------------------------------------------
// MAIN BOOKS SCREEN
// ----------------------------------------------------

@Composable
fun BooksScreen(
    onBuyBook: (AgricultureBook) -> Unit = {},
    onReadBook: (AgricultureBook) -> Unit = {}
) {
    var selectedCategory by remember {
        mutableStateOf<BookCategory?>(null)
    }

    var selectedBook by remember {
        mutableStateOf<AgricultureBook?>(null)
    }

    when {
        selectedBook != null -> {
            BookDetailsScreen(
                book = selectedBook!!,
                onBack = { selectedBook = null },
                onBuyClick = {
                    onBuyBook(selectedBook!!)
                },
                onReadClick = {
                    onReadBook(selectedBook!!)
                }
            )
        }

        selectedCategory != null -> {
            CategoryBooksScreen(
                category = selectedCategory!!,
                onBack = { selectedCategory = null },
                onBookClick = { book ->
                    selectedBook = book
                }
            )
        }

        else -> {
            BooksHomeScreen(
                categories = agricultureCategories,
                onCategoryClick = { category ->
                    selectedCategory = category
                },
                onBookClick = { book ->
                    selectedBook = book
                }
            )
        }
    }
}

// ----------------------------------------------------
// BOOKS HOME
// ----------------------------------------------------

@Composable
private fun BooksHomeScreen(
    categories: List<BookCategory>,
    onCategoryClick: (BookCategory) -> Unit,
    onBookClick: (AgricultureBook) -> Unit
) {
    var searchText by remember {
        mutableStateOf("")
    }

    val filteredCategories = categories.mapNotNull { category ->
        val matchingBooks = category.books.filter {
            it.title.contains(searchText, ignoreCase = true)
        }

        if (searchText.isBlank()) {
            category
        } else if (matchingBooks.isNotEmpty()) {
            category.copy(books = matchingBooks)
        } else {
            null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AgricultureBackground)
    ) {
        // Header
        /*Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(AgricultureGreen)
                .padding(horizontal = 20.dp, vertical = 22.dp)
        ) {
            Text(
                text = "Agriculture Library",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Knowledge for better farming",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.9f)
            )
        }*/

        // Search field
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            placeholder = {
                Text("Search agriculture books")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )

        if (filteredCategories.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No books found")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                items(
                    items = filteredCategories,
                    key = { it.name }
                ) { category ->

                    Column {
                        CategoryHeader(
                            category = category,
                            onViewAll = {
                                onCategoryClick(category)
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        // Four books in a horizontally scrollable row.
                        // Additional books are available via View All.
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(
                                    rememberScrollState()
                                )
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            category.books
                                .take(4)
                                .forEach { book ->

                                    AgricultureBookCard(
                                        book = book,
                                        modifier = Modifier.width(145.dp),
                                        onClick = {
                                            onBookClick(book)
                                        }
                                    )
                                }
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// CATEGORY HEADER
// ----------------------------------------------------

@Composable
private fun CategoryHeader(
    category: BookCategory,
    onViewAll: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "${category.emoji}  ${category.name}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = AgricultureDark
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = category.subtitle,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Text(
            text = "View All →",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = AgricultureGreen,
            modifier = Modifier.clickable {
                onViewAll()
            }
        )
    }
}

// ----------------------------------------------------
// INDIVIDUAL BOOK CARD
// ----------------------------------------------------

@Composable
private fun AgricultureBookCard(
    book: AgricultureBook,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            // Placeholder book cover.
            // Replace this with AsyncImage when image URLs are available.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFE6F1E4)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = book.emoji,
                        fontSize = 38.sp
                    )

                    Spacer(
                        modifier = Modifier.height(5.dp)
                    )

                    Text(
                        text = "AGRICULTURE",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgricultureGreen
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = book.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = AgricultureText,
                maxLines = 2,
                minLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = "By ${book.author}",
                fontSize = 10.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "₹${book.price}",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = AgricultureGreen
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(
                    horizontal = 4.dp,
                    vertical = 6.dp
                ),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AgricultureGreen
                )
            ) {
                Text(
                    text = "View Book",
                    fontSize = 11.sp
                )
            }
        }
    }
}

// ----------------------------------------------------
// VIEW ALL BOOKS IN A CATEGORY
// ----------------------------------------------------

@Composable
private fun CategoryBooksScreen(
    category: BookCategory,
    onBack: () -> Unit,
    onBookClick: (AgricultureBook) -> Unit
) {
    var searchText by remember {
        mutableStateOf("")
    }

    val filteredBooks = category.books.filter {
        it.title.contains(searchText, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AgricultureBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AgricultureGreen)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Column {
                Text(
                    text = category.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "${category.books.size} books",
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = {
                Text("Search in this category")
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        if (filteredBooks.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No books found")
            }
        } else {
            // Two-column grid without requiring an additional grid dependency.
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = filteredBooks.chunked(2),
                    key = { row -> row.first().id }
                ) { rowBooks ->

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowBooks.forEach { book ->
                            AgricultureBookCard(
                                book = book,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    onBookClick(book)
                                }
                            )
                        }

                        if (rowBooks.size == 1) {
                            Spacer(
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// BOOK DETAILS
// ----------------------------------------------------

@Composable
private fun BookDetailsScreen(
    book: AgricultureBook,
    onBack: () -> Unit,
    onBuyClick: () -> Unit,
    onReadClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AgricultureBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AgricultureGreen)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Text(
                text = "Book Details",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFE0EEDC)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = book.emoji,
                        fontSize = 75.sp
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = "ERUVAAKA AGRICULTURE LIBRARY",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgricultureGreen
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = book.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = AgricultureDark
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "By ${book.author}",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Surface(
                color = Color(0xFFE2F2E4),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = book.category,
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 8.dp
                    ),
                    color = AgricultureGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = "About this book",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = book.description,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = AgricultureText
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = "₹${book.price}",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AgricultureGreen
            )

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = onBuyClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AgricultureGreen
                )
            ) {
                Text(
                    text = "Buy This Book",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            OutlinedButton(
                onClick = onReadClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text("Read Book")
            }
        }
    }
}