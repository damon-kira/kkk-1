package com.kira.learning.module.ocr

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.edmodo.cropper.CropImageView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

/** 图片 OCR 识别 Route (纯 Compose) */
@Composable
fun ImageOcrRoute(
    modifier: Modifier = Modifier,
    openDrawer: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var sourceImageUri by remember { mutableStateOf<Uri?>(null) }
    var croppedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var ocrText by remember { mutableStateOf("") }
    var processing by remember { mutableStateOf(false) }
    var showCropDialog by remember { mutableStateOf(false) }
    var showAskCrop by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    // 权限
    val galleryPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE
    val cameraPermission = Manifest.permission.CAMERA

    var pendingAction by remember { mutableStateOf<OcrAction?>(null) }

    // 选择图片 Launcher 先声明（供权限回调使用）
    val pickLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            sourceImageUri = uri
            showAskCrop = true
        }
    }
    // 拍照 Launcher
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }
    val takePhotoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            sourceImageUri = tempPhotoUri
            showAskCrop = true
        }
    }

    fun takePhoto() {
        val resolver = context.contentResolver
        val name = "IMG_" + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date()) + ".jpg"
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, name)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        tempPhotoUri = uri
        if (uri != null) takePhotoLauncher.launch(uri)
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        val granted = result.all { it.value }
        val action = pendingAction
        if (granted && action == OcrAction.TakePhoto) {
            takePhoto()
        }
        pendingAction = null
    }

    fun ensurePermissions(forAction: OcrAction) {
        when (forAction) {
            OcrAction.Pick -> {
                // 直接调用系统选择器（ACTION_GET_CONTENT 不再强制需要读权限）
                pickLauncher.launch("image/*")
            }
            OcrAction.TakePhoto -> {
                val missing = listOf(cameraPermission).filter { context.checkSelfPermissionCompat(it) != android.content.pm.PackageManager.PERMISSION_GRANTED }
                if (missing.isNotEmpty()) {
                    pendingAction = OcrAction.TakePhoto
                    permissionLauncher.launch(missing.toTypedArray())
                } else {
                    takePhoto()
                }
            }
        }
    }

    fun startOcr(uri: Uri?, bitmap: Bitmap?) {
        if (uri == null && bitmap == null) return
        processing = true
        error = null
        ocrText = ""
        scope.launch(Dispatchers.IO) {
            try {
                val image = if (bitmap != null) InputImage.fromBitmap(bitmap, 0) else InputImage.fromFilePath(context, uri!!)
                val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                val result = recognizer.process(image).await()
                ocrText = result.text
            } catch (e: Exception) {
                error = e.message
            } finally {
                processing = false
            }
        }
    }

    if (showAskCrop) {
        AlertDialog(
            onDismissRequest = { showAskCrop = false },
            confirmButton = {
                TextButton({ showAskCrop = false; showCropDialog = true }) { Text("裁切") }
            },
            dismissButton = {
                TextButton({ showAskCrop = false; croppedBitmap = null; startOcr(sourceImageUri, null) }) { Text("直接识别") }
            },
            title = { Text("是否裁切图片?") }
        )
    }

    if (showCropDialog && sourceImageUri != null) {
        CropDialog(
            source = sourceImageUri!!,
            onCancel = { showCropDialog = false },
            onCropped = { bmp ->
                croppedBitmap = bmp
                showCropDialog = false
                startOcr(null, bmp)
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { ensurePermissions(OcrAction.Pick) }) { Text("选择图片") }
            Button(onClick = { ensurePermissions(OcrAction.TakePhoto) }) { Text("拍照") }
            OutlinedButton(onClick = openDrawer) { Text("菜单") }
        }
        Spacer(Modifier.height(20.dp))
        sourceImageUri?.let {
            Text("原图:", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            AsyncImage(uri = it)
            Spacer(Modifier.height(16.dp))
        }
        croppedBitmap?.let {
            Text("裁切后:", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Image(bitmap = it.asImageBitmap(), contentDescription = null, modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp))
            Spacer(Modifier.height(16.dp))
        }
        if (processing) {
            CircularProgressIndicator(); Spacer(Modifier.height(8.dp)); Text("识别中...")
        }
        error?.let { Text("错误: $it", color = MaterialTheme.colorScheme.error) }
        if (ocrText.isNotBlank()) {
            Text("识别结果:", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(ocrText)
        }
    }
}

private enum class OcrAction { Pick, TakePhoto }

@Composable
private fun AsyncImage(uri: Uri) {
    val context = LocalContext.current
    var bitmap by remember(uri) { mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(uri) {
        bitmap = context.contentResolver.openInputStream(uri)?.use { input ->
            android.graphics.BitmapFactory.decodeStream(input)
        }
    }
    bitmap?.let {
        Image(bitmap = it.asImageBitmap(), contentDescription = null, modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp))
    }
}

@Composable
private fun CropDialog(source: Uri, onCancel: () -> Unit, onCropped: (Bitmap) -> Unit) {
    Dialog(onDismissRequest = onCancel) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column(Modifier.padding(16.dp)) {
                var cropImageView by remember { mutableStateOf<CropImageView?>(null) }
                AndroidCropper(source = source, onReady = { cropImageView = it })
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onCancel) { Text("取消") }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = { cropImageView?.croppedImage?.let(onCropped) }) { Text("完成") }
                }
            }
        }
    }
}

@Composable
private fun AndroidCropper(source: Uri, onReady: (CropImageView) -> Unit) {
    AndroidView(factory = { ctx ->
        CropImageView(ctx).apply {
            setFixedAspectRatio(false)
            val bitmap = ctx.contentResolver.openInputStream(source)?.use { input ->
                android.graphics.BitmapFactory.decodeStream(input)
            }
            setImageBitmap(bitmap)
            onReady(this)
        }
    })
}

private suspend fun <T> com.google.android.gms.tasks.Task<T>.await(): T = suspendCancellableCoroutine { cont ->
    addOnCompleteListener { task ->
        if (!cont.isActive) return@addOnCompleteListener
        if (task.isSuccessful) cont.resume(task.result) else cont.resumeWithException(task.exception ?: RuntimeException("Task failed"))
    }
}

private fun Context.checkSelfPermissionCompat(permission: String): Int =
    androidx.core.content.ContextCompat.checkSelfPermission(this, permission)

@Suppress("unused")
private fun saveBitmap(context: Context, bitmap: Bitmap): Uri? = try {
    val name = "CROP_" + System.currentTimeMillis() + ".jpg"
    val file = File(context.cacheDir, name)
    FileOutputStream(file).use { fos -> bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos) }
    androidx.core.content.FileProvider.getUriForFile(context, context.packageName + ".provider", file)
} catch (_: Exception) { null }
