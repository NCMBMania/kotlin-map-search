package com.example.kotlinfirstdemo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nifcloud.mbaas.core.NCMBFile
import com.nifcloud.mbaas.core.NCMBObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*


@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun FormScreen() {
    var imageUri = remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            imageUri.value = uri
            val source = ImageDecoder.createSource(context.contentResolver, uri!!)
            bitmap = ImageDecoder.decodeBitmap(source)
        }
    }
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
       AlertDialog(
           onDismissRequest = {},
           buttons = {
               Button(onClick = {
                   showDialog = false
               }) {
                   Text("OK")
               }
           },
           title = {Text("保存完了")},
           text = {Text("保存完了しました")}
       )
    }
    var memo by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (imageUri.value == null) {
            IconButton(
                onClick = {
                    launcher.launch("image/*")
                },
                modifier = Modifier.size(300.dp)
            ) {
                Icon(Icons.Rounded.Image, "デフォルトアイコン", Modifier.size(size = 300.dp))
            }
        } else {
            Image(
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = "選択した写真",
                Modifier.clickable(
                    onClick = {
                        launcher.launch("image/*")
                    }
                )
            )
        }
        Text("写真を選択して、メモを入力してください")
        OutlinedTextField(
            value = memo,
            onValueChange = { memo = it },
            modifier = Modifier.padding(20.dp),
            maxLines = 3,
        )
        Button(
            onClick = {
                save(context, bitmap!!, memo, imageUri.value!!)
                showDialog = true
            }
        ){
            Text(text = "メモを保存する")
        }
    }
}

fun save(context: Context, bitmap: Bitmap, memo: String, imageUri: Uri) {
    val uuidString = UUID.randomUUID().toString()
    val match = Regex("^.*\\.(.*)$").find(imageUri.toString())
    val extension = if (match != null) match!!.groups[1]!!.value.lowercase() else "png"
    val fileName = "${uuidString}.${extension}"
    val file = NCMBFile(fileName = fileName, fileData = getFile(uuidString, extension, bitmap!!, context = context))
    file.save()
    // 次にメモを保存
    val obj = NCMBObject("Memo")
    obj.put("text", memo)
    obj.put("fileName", fileName)
    obj.save()
}

fun getFile(fileName: String, extension: String, bitmap: Bitmap, context: Context): File {
    val outputDir = context.cacheDir
    val outputFile = File.createTempFile(fileName, ".${extension}", outputDir)
    val stream = ByteArrayOutputStream()
    if (extension == "jpg" || extension == "jpeg") {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
    } else if (extension == "png") {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    }
    outputFile.writeBytes(stream.toByteArray())
    return outputFile
}