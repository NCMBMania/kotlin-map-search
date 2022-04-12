package com.example.kotlinfirstdemo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nifcloud.mbaas.core.NCMBCallback
import com.nifcloud.mbaas.core.NCMBFile
import com.nifcloud.mbaas.core.NCMBObject
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.graphics.asImageBitmap

@Composable
fun ListRow(obj: NCMBObject) {
    var bitmap = remember { mutableStateOf<Bitmap?>(null) }
    val file = NCMBFile(obj.getString("fileName")!!)
    file.fetchInBackground(NCMBCallback { e, data ->
        if (e == null && file.fileDownloadByte != null) {
            val data = file.fileDownloadByte!!
            bitmap.value = BitmapFactory.decodeByteArray(data, 0, data.size)
        }
    })
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(Color.White, RoundedCornerShape(5.dp))
    ) {
        if (bitmap.value != null) {
            Image(
                bitmap = bitmap.value!!.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .padding(10.dp)
            )
        } else {
            Icon(Icons.Rounded.Image, "説明", Modifier.size(size = 150.dp))
        }
        Text(obj.getString("text")!!)
    }
}