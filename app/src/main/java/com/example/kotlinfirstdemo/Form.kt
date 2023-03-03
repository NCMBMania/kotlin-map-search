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
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*


@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun FormScreen() {
    var imageUri = remember { mutableStateOf<Uri?>(null) }   // 画像を指定した場合のURIが入る
    var bitmap by remember { mutableStateOf<Bitmap?>(null) } // 画像データが入る
    var memo by remember { mutableStateOf("") } // メモのテキスト文が入る
    var showDialog by remember { mutableStateOf(false) } // ダイアログを表示する場合 true にする
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        // 画像があれば、ビットマップデータ化する
        if (uri != null) {
            imageUri.value = uri
            val source = ImageDecoder.createSource(context.contentResolver, uri!!)
            bitmap = ImageDecoder.decodeBitmap(source)
        }
    }

    // ダイアログを表示するフロー
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

    // 画面について
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

// 入力されたテキスト、画像データをNCMBに保存する関数
fun save(context: Context, bitmap: Bitmap, memo: String, imageUri: Uri) {
}

// ファイルのデータを返す
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