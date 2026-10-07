
package com.example.myapplication.Utils

/*import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File*/
import kotlin.math.max
import kotlin.math.min

/*
data class PdfPage(
    val pageNumber: Int,
    val bitmap: Bitmap
)

@Composable
fun PdfReaderScreen(
    pdfFile: File,
    onBack: () -> Unit
) {

    var pages by remember {
        mutableStateOf<List<PdfPage>>(emptyList())
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(pdfFile) {

        isLoading = true

        pages = withContext(Dispatchers.IO) {

            renderPdfPages(pdfFile)
        }

        isLoading = false
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        if (isLoading) {

            Text(
                text = "Loading magazine...",
                modifier = Modifier.align(Alignment.Center)
            )

        } else {

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

                items(
                    items = pages,
                    key = { it.pageNumber }
                ) { page ->

                    ZoomablePdfPage(
                        bitmap = page.bitmap
                    )
                }
            }
        }

        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart)
        ) {

            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
        }
    }
}


private fun renderPdfPages(
    pdfFile: File
): List<PdfPage> {

    val pages = mutableListOf<PdfPage>()

    val descriptor = ParcelFileDescriptor.open(
        pdfFile,
        ParcelFileDescriptor.MODE_READ_ONLY
    )

    val renderer = PdfRenderer(descriptor)

    try {

        for (index in 0 until renderer.pageCount) {

            val page = renderer.openPage(index)

            val scale = 1.5f

            val width = (page.width * scale).toInt()
            val height = (page.height * scale).toInt()

            val bitmap = Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.ARGB_8888
            )

            bitmap.eraseColor(Color.WHITE)

            page.render(
                bitmap,
                null,
                null,
                PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
            )

            page.close()

            pages.add(
                PdfPage(
                    pageNumber = index + 1,
                    bitmap = bitmap
                )
            )
        }

    } finally {

        renderer.close()
        descriptor.close()
    }

    return pages
}



@Composable
private fun ZoomablePdfPage(
    bitmap: Bitmap
) {

    var scale by remember {
        mutableFloatStateOf(1f)
    }

    var offset by remember {
        mutableStateOf(Offset.Zero)
    }

    val imageBitmap = remember(bitmap) {
        bitmap.asImageBitmap()
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {

                detectTransformGestures { _, pan, zoom, _ ->

                    scale = (scale * zoom)
                        .coerceIn(1f, 4f)

                    if (scale > 1f) {
                        offset += pan
                    } else {
                        offset = Offset.Zero
                    }
                }
            }
            .pointerInput(Unit) {

                detectTapGestures(
                    onDoubleTap = {

                        scale =
                            if (scale > 1f) {
                                1f
                            } else {
                                2.5f
                            }

                        if (scale == 1f) {
                            offset = Offset.Zero
                        }
                    }
                )
            }
            .graphicsLayer {

                scaleX = scale
                scaleY = scale

                translationX = offset.x
                translationY = offset.y
            }
    ) {

        drawImage(
            image = imageBitmap
        )
    }
}
*/



import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.graphics.Color as ComposeColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.math.roundToInt

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable

import android.content.Context
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class PdfReaderView @JvmOverloads constructor(
    context: Context,
    private var pdfFile: File? = null,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = VERTICAL
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        pdfFile?.let { renderPdf(it) }
    }

    fun setPdfFile(file: File) {
        this.pdfFile = file
        removeAllViews()
        renderPdf(file)
    }

    private fun renderPdf(file: File) {
        if (!file.exists() || file.length() == 0L) {
            showError("File does not exist or is empty.")
            return
        }

        val magazineStorage = MagazineStorage(context)
        if (!magazineStorage.isValidDocument(file)) {
            file.delete()
            showError("Invalid or corrupted file. Please re-download.")
            return
        }

        // 1. Check if file is an Image (JPEG, PNG, WEBP, etc.)
        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(file.absolutePath, options)
        if (options.outWidth > 0 && options.outHeight > 0) {
            try {
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                if (bitmap != null) {
                    val imageView = ImageView(context).apply {
                        layoutParams = LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT
                        )
                        adjustViewBounds = true
                        scaleType = ImageView.ScaleType.FIT_CENTER
                        setImageBitmap(bitmap)
                    }
                    addView(imageView)
                    return
                }
            } catch (e: Exception) {
                Log.e("PdfReaderView", "Failed to render image file", e)
            }
        }

        try {
            val descriptor = ParcelFileDescriptor.open(
                file,
                ParcelFileDescriptor.MODE_READ_ONLY
            )
            val renderer = PdfRenderer(descriptor)

            if (renderer.pageCount == 0) {
                showError("PDF file contains no pages.")
                renderer.close()
                descriptor.close()
                return
            }

            val displayMetrics = context.resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels

            for (i in 0 until renderer.pageCount) {
                val page = renderer.openPage(i)
                val scale = if (page.width > 0) (screenWidth.toFloat() / page.width.toFloat()).coerceAtLeast(1f) else 2f
                val width = (page.width * scale).toInt().coerceAtLeast(1)
                val height = (page.height * scale).toInt().coerceAtLeast(1)

                val bitmap = Bitmap.createBitmap(
                    width,
                    height,
                    Bitmap.Config.ARGB_8888
                )
                bitmap.eraseColor(Color.WHITE)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                page.close()

                val imageView = ImageView(context).apply {
                    layoutParams = LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 0, 0, 16)
                    }
                    adjustViewBounds = true
                    scaleType = ImageView.ScaleType.FIT_CENTER
                    setImageBitmap(bitmap)
                }
                addView(imageView)
            }

            renderer.close()
            descriptor.close()
        } catch (e: Exception) {
            Log.e("PdfReaderView", "Error rendering PDF: ${file.absolutePath}", e)
            showError("Failed to render PDF: ${e.message}")
        }
    }

    private fun showError(message: String) {
        removeAllViews()
        val textView = TextView(context).apply {
            text = message
            textSize = 16f
            setTextColor(Color.RED)
            setPadding(32, 32, 32, 32)
        }
        addView(textView)
    }
}

@Composable
fun PdfReaderScreen(
    pdfFile: File,
    onBack: () -> Unit
) {

    if (!pdfFile.exists()) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("PDF file not found")
        }

        return
    }

    val pdfRenderer = remember(pdfFile) {
        PdfRendererManager(pdfFile)
    }

    DisposableEffect(pdfFile) {
        onDispose {
            pdfRenderer.close()
        }
    }

    var pageCount by remember {
        mutableStateOf(0)
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(pdfFile) {

        pageCount = withContext(Dispatchers.IO) {
            pdfRenderer.pageCount()
        }

        isLoading = false
    }

    if (isLoading) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading magazine...")
        }

        return
    }

    if (pageCount == 0) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No pages found")
        }

        return
    }

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = {
            pageCount
        }
    )

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ComposeColor.Black)
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { pageIndex ->

            PdfPage(
                pageIndex = pageIndex,
                renderer = pdfRenderer
            )
        }

        /*
         * Top bar
         */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(
                    top = 8.dp,
                    start = 8.dp,
                    end = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBack
            ) {

                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = ComposeColor.White
                )
            }

            Text(
                text = "E-Magazine",
                color = ComposeColor.White,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "${pagerState.currentPage + 1} / $pageCount",
                color = ComposeColor.White
            )
        }

        /*
         * Bottom navigation
         */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                enabled = pagerState.currentPage > 0,
                onClick = {

                    scope.launch {
                        pagerState.animateScrollToPage(
                            pagerState.currentPage - 1
                        )
                    }
                }
            ) {

                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = "Previous page",
                    tint = ComposeColor.White
                )
            }

            Text(
                text = "${pagerState.currentPage + 1} / $pageCount",
                color = ComposeColor.White
            )

            IconButton(
                enabled = pagerState.currentPage < pageCount - 1,
                onClick = {

                    scope.launch {
                        pagerState.animateScrollToPage(
                            pagerState.currentPage + 1
                        )
                    }
                }
            ) {

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Next page",
                    tint = ComposeColor.White
                )
            }
        }
    }
}

//working
@Composable
private fun PdfPage(
    pageIndex: Int,
    renderer: PdfRendererManager
) {
    var bitmap by remember(pageIndex) {
        mutableStateOf<Bitmap?>(null)
    }

    var scale by remember(pageIndex) {
        mutableFloatStateOf(1f)
    }

    var offsetX by remember(pageIndex) {
        mutableFloatStateOf(0f)
    }

    var offsetY by remember(pageIndex) {
        mutableFloatStateOf(0f)
    }

    // ---------------------------------------------------------
    // Load PDF page
    // ---------------------------------------------------------
    LaunchedEffect(pageIndex) {
        bitmap = withContext(Dispatchers.IO) {
            renderer.renderPage(pageIndex)
        }
    }

    // ---------------------------------------------------------
    // Clean bitmap
    // ---------------------------------------------------------
    DisposableEffect(pageIndex) {
        onDispose {
            bitmap?.recycle()
            bitmap = null
        }
    }

    val pageBitmap = bitmap ?: return

    val imageBitmap = remember(pageBitmap) {
        pageBitmap.asImageBitmap()
    }

    // ---------------------------------------------------------
    // Pinch Zoom + Pan
    // ---------------------------------------------------------
    val transformState = rememberTransformableState { zoomChange, panChange, _ ->

        // Apply pinch zoom
        val newScale = (scale * zoomChange)
            .coerceIn(1f, 4f)

        scale = newScale

        // -----------------------------------------------------
        // Only allow 1-finger / pan movement when zoomed
        // -----------------------------------------------------
        if (scale > 1f) {

            offsetX += panChange.x
            offsetY += panChange.y

        } else {

            // When returning to 1x reset position
            offsetX = 0f
            offsetY = 0f
        }
    }

    // ---------------------------------------------------------
    // PDF Image
    // ---------------------------------------------------------
    Image(
        bitmap = imageBitmap,
        contentDescription = "Magazine page",
        contentScale = ContentScale.Fit,

        modifier = Modifier
            .fillMaxWidth()

            // -------------------------------------------------
            // Double Tap
            // 1x  -> 2.5x
            // >1x -> 1x
            // -------------------------------------------------
            .pointerInput(Unit) {

                detectTapGestures(
                    onDoubleTap = {

                        if (scale == 1f) {

                            scale = 2.5f

                        } else {

                            scale = 1f
                            offsetX = 0f
                            offsetY = 0f
                        }
                    }
                )
            }

            // -------------------------------------------------
            // Pinch + Pan
            // -------------------------------------------------
            .transformable(
                state = transformState,

                // VERY IMPORTANT
                //
                // At 1x:
                //     don't consume one-finger horizontal drag
                //     → HorizontalPager gets the swipe
                //
                // At >1x:
                //     allow pan
                //
                canPan = {
                    scale > 1f
                }
            )

            // -------------------------------------------------
            // Apply zoom / translation
            // -------------------------------------------------
            .graphicsLayer {

                scaleX = scale
                scaleY = scale

                translationX = offsetX
                translationY = offsetY
            }
    )
}

/*@Composable
private fun PdfPage(
    pageIndex: Int,
    renderer: PdfRendererManager
) {

    var bitmap by remember(pageIndex) {
        mutableStateOf<Bitmap?>(null)
    }

    var scale by remember(pageIndex) {
        mutableFloatStateOf(1f)
    }

    var offsetX by remember(pageIndex) {
        mutableFloatStateOf(0f)
    }

    var offsetY by remember(pageIndex) {
        mutableFloatStateOf(0f)
    }

    LaunchedEffect(pageIndex) {

        bitmap = withContext(Dispatchers.IO) {
            renderer.renderPage(pageIndex)
        }
    }

    DisposableEffect(pageIndex) {

        onDispose {
            bitmap?.recycle()
            bitmap = null
        }
    }

    val pageBitmap = bitmap ?: return

    val imageBitmap = remember(pageBitmap) {
        pageBitmap.asImageBitmap()
    }

    *//*Image(
        bitmap = imageBitmap,
        contentDescription = "Magazine page",
        contentScale = ContentScale.Fit,

        modifier = Modifier
            .fillMaxWidth()

            // Double tap
            .pointerInput(Unit) {

                detectTapGestures(
                    onDoubleTap = {

                        if (scale > 1f) {

                            scale = 1f
                            offsetX = 0f
                            offsetY = 0f

                        } else {

                            scale = 2.5f
                        }
                    }
                )
            }

            // Pinch + pan
            .pointerInput(scale) {

                detectTransformGestures(
                    panZoomLock = false
                ) { centroid, pan, zoom, rotation ->

                    *//**//*
                     * IMPORTANT:
                     *
                     * At 1x:
                     *  - 1 finger swipe must go to HorizontalPager
                     *  - 2 finger pinch should zoom
                     *
                     * When zoomed:
                     *  - pan the image
                     *  - pinch to zoom
                     *//**//*

                    if (scale > 1f) {

                        // Already zoomed
                        scale = (scale * zoom)
                            .coerceIn(1f, 4f)

                        offsetX += pan.x
                        offsetY += pan.y

                        if (scale <= 1f) {

                            scale = 1f
                            offsetX = 0f
                            offsetY = 0f
                        }

                    } else {

                        *//**//*
                         * At 1x we only want zoom.
                         *
                         * Don't use pan here.
                         *
                         * The important part is:
                         * pinch zoom changes zoom,
                         * while normal horizontal swipe
                         * remains available to HorizontalPager.
                         *//**//*

                        if (zoom != 1f) {

                            scale = (scale * zoom)
                                .coerceIn(1f, 4f)
                        }
                    }
                }
            }

            .graphicsLayer {

                scaleX = scale
                scaleY = scale

                translationX = offsetX
                translationY = offsetY
            }
    )*//*

    Image(
        bitmap = imageBitmap,
        contentDescription = "Magazine page",
        contentScale = ContentScale.Fit,

        modifier = Modifier
            .fillMaxWidth()


             // Only handle pan + pinch when zoomed.

            .then(

                if (scale > 1f) {

                    Modifier.pointerInput(scale) {

                        detectTransformGestures {
                                _,
                                pan,
                                zoom,
                                _ ->

                            scale = (scale * zoom)
                                .coerceIn(1f, 4f)

                            if (scale > 1f) {

                                offsetX += pan.x
                                offsetY += pan.y

                            } else {

                                scale = 1f

                                offsetX = 0f
                                offsetY = 0f
                            }
                        }
                    }

                } else {

                    Modifier
                }
            )


            .pointerInput(Unit) {

                detectTapGestures(
                    onDoubleTap = {

                        if (scale > 1f) {

                            scale = 1f

                            offsetX = 0f
                            offsetY = 0f

                        } else {

                            scale = 2.5f
                        }
                    }
                )
            }


            .graphicsLayer {

                scaleX = scale
                scaleY = scale

                translationX = offsetX
                translationY = offsetY
            }
    )
}*/



/*
 * PDF PAGE
 */
/*@Composable
private fun PdfPage(
    pageIndex: Int,
    renderer: PdfRendererManager
) {

    var bitmap by remember(pageIndex) {
        mutableStateOf<Bitmap?>(null)
    }

    var scale by remember(pageIndex) {
        mutableFloatStateOf(1f)
    }

    var offsetX by remember(pageIndex) {
        mutableFloatStateOf(0f)
    }

    var offsetY by remember(pageIndex) {
        mutableFloatStateOf(0f)
    }

    *//*
     * Render page in background
     *//*
    LaunchedEffect(pageIndex) {

        bitmap = withContext(Dispatchers.IO) {

            renderer.renderPage(pageIndex)
        }
    }

    *//*
     * Release bitmap when page leaves composition
     *//*
    DisposableEffect(pageIndex) {

        onDispose {

            bitmap?.recycle()
            bitmap = null
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        val pageBitmap = bitmap

        if (pageBitmap == null) {

            Text(
                text = "Loading...",
                color = ComposeColor.White
            )

        } else {

            val imageBitmap = remember(pageBitmap) {
                pageBitmap.asImageBitmap()
            }

            Image(
                bitmap = imageBitmap,
                contentDescription = "Magazine page",

                contentScale = ContentScale.Fit,

                modifier = Modifier
                    .fillMaxWidth()

                    *//*
                     * Pinch + Pan
                     *//*
                    .pointerInput(Unit) {

                        detectTransformGestures {
                                _,
                                pan,
                                zoom,
                                _ ->

                            val newScale =
                                (scale * zoom)
                                    .coerceIn(
                                        1f,
                                        4f
                                    )

                            scale = newScale

                            if (newScale > 1f) {

                                offsetX += pan.x
                                offsetY += pan.y

                            } else {

                                offsetX = 0f
                                offsetY = 0f
                            }
                        }
                    }

                    *//*
                     * Double tap
                     *//*
                    .pointerInput(Unit) {

                        detectTapGestures(
                            onDoubleTap = {

                                if (scale > 1f) {

                                    scale = 1f

                                    offsetX = 0f
                                    offsetY = 0f

                                } else {

                                    scale = 2.5f
                                }
                            }
                        )
                    }

                    *//*
                     * Apply zoom + pan
                     *//*
                    .graphicsLayer {

                        scaleX = scale
                        scaleY = scale

                        translationX = offsetX
                        translationY = offsetY
                    }
            )
        }
    }
}*/


/*
 * PDF RENDERER MANAGER
 */
private class PdfRendererManager(
    private val pdfFile: File
) {

    private var descriptor: ParcelFileDescriptor? = null
    private var renderer: PdfRenderer? = null
    private var isImage = false

    init {
        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(pdfFile.absolutePath, options)
        if (options.outWidth > 0 && options.outHeight > 0) {
            isImage = true
        } else {
            try {
                descriptor = ParcelFileDescriptor.open(
                    pdfFile,
                    ParcelFileDescriptor.MODE_READ_ONLY
                )
                renderer = PdfRenderer(descriptor!!)
            } catch (e: Exception) {
                Log.e("PdfRendererManager", "Failed to open PDF descriptor", e)
            }
        }
    }

    fun pageCount(): Int {
        if (isImage) return 1
        return renderer?.pageCount ?: 0
    }

    fun renderPage(
        pageIndex: Int
    ): Bitmap? {
        if (isImage) {
            return try {
                BitmapFactory.decodeFile(pdfFile.absolutePath)
            } catch (e: Exception) {
                Log.e("PdfRendererManager", "Failed to decode image file", e)
                null
            }
        }

        val pdfRenderer = renderer
            ?: return null

        if (
            pageIndex < 0 ||
            pageIndex >= pdfRenderer.pageCount
        ) {
            return null
        }

        return try {
            val page = pdfRenderer.openPage(pageIndex)

            val scale = 2f

            val width = (page.width * scale).toInt().coerceAtLeast(1)
            val height = (page.height * scale).toInt().coerceAtLeast(1)

            val bitmap = Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.ARGB_8888
            )

            bitmap.eraseColor(Color.WHITE)

            page.render(
                bitmap,
                null,
                null,
                PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
            )

            page.close()

            bitmap
        } catch (e: Exception) {
            Log.e("PdfRendererManager", "Error rendering page $pageIndex", e)
            null
        }
    }

    fun close() {
        renderer?.close()
        renderer = null

        descriptor?.close()
        descriptor = null
    }
}




