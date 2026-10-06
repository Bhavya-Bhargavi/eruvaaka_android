package com.example.myapplication.ui

import android.content.Context
import android.widget.ScrollView
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.myapplication.Utils.MagazineStorage
import com.example.myapplication.Utils.PdfReaderView
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

@Composable
fun PdfViewerScreen(
    file: File
) {

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->

            ScrollView(context).apply {

                addView(
                    PdfReaderView(
                        context = context,
                        pdfFile = file
                    )
                )
            }
        }
    )
}
