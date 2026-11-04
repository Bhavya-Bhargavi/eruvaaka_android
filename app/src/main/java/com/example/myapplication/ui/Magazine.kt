package com.example.myapplication.ui

import android.content.Context
import android.graphics.BitmapFactory
import android.os.SystemClock
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.Utils.PdfReaderScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale

data class EMagazine(
    val id: String,
    val title: String,
    val month: String,
    val year: Int,
    val pdfUrl: String,
    val coverUrl: String = ""
)

val magazineList = listOf(
    EMagazine(
        id = "eru_2026_10",
        title = "Eruvaaka Monthly Magazine",
        month = "October",
        year = 2026,
        pdfUrl = "https://your-domain.com/magazines/2026/october.pdf",
        coverUrl = "https://your-domain.com/covers/2026/october.jpg"
    ),
    EMagazine(
        id = "eru_2026_09",
        title = "Eruvaaka Monthly Magazine",
        month = "September",
        year = 2026,
        pdfUrl = "https://your-domain.com/magazines/2026/september.pdf",
        coverUrl = "https://your-domain.com/covers/2026/september.jpg"
    ),
    EMagazine(
        id = "eru_2026_08",
        title = "Eruvaaka Monthly Magazine",
        month = "August",
        year = 2026,
        pdfUrl = "https://your-domain.com/magazines/2026/august.pdf",
        coverUrl = "https://your-domain.com/covers/2026/august.jpg"
    )
)

@Composable
fun EMagazineScreen(
    magazines: List<EMagazine>,
    onDownloadMagazine: (EMagazine) -> Unit,
    onOpenPdf: (File) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val sortedMagazines = remember(magazines) {
        magazines.sortedWith(
            compareByDescending<EMagazine> { it.year }
                .thenByDescending { monthNumber(it.month) }
        )
    }

    val downloadedFiles = remember(magazines) {
        mutableStateMapOf<String, File>()
    }

    val downloading = remember {
        mutableStateMapOf<String, Boolean>()
    }

    val progress = remember {
        mutableStateMapOf<String, Int>()
    }

    val errors = remember {
        mutableStateMapOf<String, String>()
    }

    // Check which magazines are already saved locally.
    LaunchedEffect(sortedMagazines) {
        sortedMagazines.forEach { magazine ->
            val file = magazineFile(context, magazine)
            if (file.exists() && file.length() > 0L) {
                downloadedFiles[magazine.id] = file
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7F2))
            .padding(16.dp)
    ) {
        Text(
            text = "E-Magazines",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF245C35)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Read the latest monthly editions of Eruvaaka.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (sortedMagazines.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No e-magazines available yet.")
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(
                    items = sortedMagazines,
                    key = { it.id }
                ) { magazine ->

                    val file = downloadedFiles[magazine.id]
                    val isDownloading = downloading[magazine.id] == true
                    val downloadProgress = progress[magazine.id] ?: 0
                    val error = errors[magazine.id]

                    EMagazineCard(
                        magazine = magazine,
                        isDownloaded = file != null,
                        isDownloading = isDownloading,
                        progress = downloadProgress,
                        error = error,
                        onClick = {
                            onDownloadMagazine(magazine)
                            /*if (file != null && file.exists()) {
                                onOpenPdf(file)
                            } else if (!isDownloading) {
                                errors.remove(magazine.id)
                                downloading[magazine.id] = true
                                progress[magazine.id] = 0

                                scope.launch {
                                    try {
                                        val downloadedFile =
                                            downloadMagazine(
                                                context = context,
                                                magazine = magazine,
                                                onProgress = { percent ->
                                                    progress[magazine.id] = percent
                                                }
                                            )

                                        downloadedFiles[magazine.id] =
                                            downloadedFile

                                        onOpenPdf(downloadedFile)
                                    } catch (e: Exception) {
                                        errors[magazine.id] =
                                            e.message
                                                ?: "Download failed. Please try again."
                                    } finally {
                                        downloading[magazine.id] = false
                                    }
                                }
                            }*/
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun EMagazineCard(
    magazine: EMagazine,
    isDownloaded: Boolean,
    isDownloading: Boolean,
    progress: Int,
    error: String?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = !isDownloading,
                onClick = onClick
            ),
        shape = RoundedCornerShape(16.dp),
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
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MagazineCover(
                magazine = magazine,
                modifier = Modifier
                    .width(100.dp)
                    .height(132.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${magazine.month} ${magazine.year}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF245C35)
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = magazine.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(10.dp))

                when {
                    isDownloading -> {
                        LinearProgressIndicator(
                            progress = { progress / 100f },
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFF2E7D32)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Downloading $progress%",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    isDownloaded -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(modifier = Modifier.width(5.dp))

                            Text(
                                text = "Downloaded · Read now",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    }

                    else -> {
                        Button(
                            onClick = onClick,
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(
                                horizontal = 8.dp,
                                vertical = 6.dp
                            ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2E7D32)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(modifier = Modifier.width(5.dp))

                            Text("Download")
                        }
                    }
                }

                if (error != null) {
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun MagazineCover(
    magazine: EMagazine,
    modifier: Modifier = Modifier
) {
    var imageBitmap by remember(magazine.coverUrl) {
        mutableStateOf<androidx.compose.ui.graphics.ImageBitmap?>(null)
    }

    LaunchedEffect(magazine.coverUrl) {
        if (magazine.coverUrl.isNotBlank()) {
            imageBitmap = withContext(Dispatchers.IO) {
                try {
                    val connection =
                        URL(magazine.coverUrl).openConnection()
                                as HttpURLConnection

                    connection.connectTimeout = 10000
                    connection.readTimeout = 10000

                    try {
                        connection.inputStream.use { input ->
                            BitmapFactory.decodeStream(input)
                                ?.asImageBitmap()
                        }
                    } finally {
                        connection.disconnect()
                    }
                } catch (_: Exception) {
                    null
                }
            }
        }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFE3F1E2)),
        contentAlignment = Alignment.Center
    ) {
        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap!!,
                contentDescription = "${magazine.title} cover",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = null,
                    tint = Color(0xFF245C35),
                    modifier = Modifier.size(38.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "ERUVAAKA",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF245C35),
                    style = MaterialTheme.typography.labelMedium
                )

                Text(
                    text = "${magazine.month} ${magazine.year}",
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

private fun magazineFile(
    context: Context,
    magazine: EMagazine
): File {
    val directory = File(context.filesDir, "emagazines")
    if (!directory.exists()) {
        directory.mkdirs()
    }

    val safeId = magazine.id.replace(
        Regex("[^A-Za-z0-9_-]"),
        "_"
    )

    return File(directory, "$safeId.pdf")
}

private suspend fun downloadMagazine(
    context: Context,
    magazine: EMagazine,
    onProgress: (Int) -> Unit
): File = withContext(Dispatchers.IO) {

    require(magazine.pdfUrl.startsWith("https://")) {
        "A valid HTTPS magazine PDF URL is required."
    }

    val targetFile = magazineFile(context, magazine)
    val temporaryFile = File(targetFile.parentFile, "${targetFile.name}.part")

    val connection =
        URL(magazine.pdfUrl).openConnection() as HttpURLConnection

    try {
        connection.requestMethod = "GET"
        connection.connectTimeout = 20000
        connection.readTimeout = 30000
        connection.instanceFollowRedirects = true
        connection.connect()

        check(connection.responseCode in 200..299) {
            "Download failed: HTTP ${connection.responseCode}"
        }

        val contentType =
            connection.contentType?.lowercase(Locale.ROOT).orEmpty()

        if (contentType.contains("text/html") ||
            contentType.contains("application/json")
        ) {
            error("The server returned a webpage instead of a PDF.")
        }

        val totalBytes = connection.contentLengthLong
        var downloadedBytes = 0L

        connection.inputStream.buffered().use { input ->
            FileOutputStream(temporaryFile).buffered().use { output ->
                val buffer = ByteArray(16 * 1024)

                while (true) {
                    val count = input.read(buffer)
                    if (count == -1) break

                    output.write(buffer, 0, count)
                    downloadedBytes += count

                    if (totalBytes > 0) {
                        val percent =
                            ((downloadedBytes * 100) / totalBytes)
                                .toInt()
                                .coerceIn(0, 100)

                        withContext(Dispatchers.Main) {
                            onProgress(percent)
                        }
                    }
                }
            }
        }

        // Basic PDF signature validation.
        val signature = ByteArray(5)
        temporaryFile.inputStream().use { input ->
            check(input.read(signature) == 5 &&
                    String(signature, Charsets.US_ASCII) == "%PDF-"
            ) {
                "The downloaded file is not a valid PDF."
            }
        }

        if (targetFile.exists()) {
            targetFile.delete()
        }

        check(temporaryFile.renameTo(targetFile)) {
            "Could not save the downloaded magazine."
        }

        withContext(Dispatchers.Main) {
            onProgress(100)
        }

        targetFile
    } finally {
        connection.disconnect()
        if (temporaryFile.exists()) {
            temporaryFile.delete()
        }
    }
}

private fun monthNumber(month: String): Int {
    return try {
        java.time.Month.valueOf(
            month.uppercase(Locale.ENGLISH)
        ).value
    } catch (_: Exception) {
        try {
            java.time.Month.entries.first {
                it.getDisplayName(
                    java.time.format.TextStyle.FULL,
                    Locale.ENGLISH
                ).equals(month, ignoreCase = true)
            }.value
        } catch (_: Exception) {
            0
        }
    }
}










/*
package com.example.myapplication.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.myapplication.Utils.MagazineStorage
import com.example.myapplication.Utils.PdfReaderScreen
import com.example.myapplication.Utils.Screen
import java.io.File

fun openMagazine(
    context: Context,
    navController: NavController,
    isSubscribed: Boolean
) {

    if (!isSubscribed) {

        navController.navigate(Screen.Subscription.route)

        return
    }

    val storage = MagazineStorage(context)

    val pdfFile = storage.getMagazineFile(
        "eru_vaaka_latest.pdf"
    )

    if (pdfFile.exists()) {

        navController.navigate(
            "${Screen.PdfViewer.route}?fileName=eru_vaaka_latest.pdf"
        )

    } else {

        Toast.makeText(
            context,
            "Magazine is not downloaded yet",
            Toast.LENGTH_SHORT
        ).show()
    }
}
*/


@Composable
fun PdfViewerScreen(
    file: File,
    onBack: () -> Unit = {}
) {
    PdfReaderScreen(
        pdfFile = file,
        onBack = onBack
    )
}



