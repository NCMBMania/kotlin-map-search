package com.example.kotlinfirstdemo

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ListScreen() {
    // ↓ エラーにならないためのダミー
    var ary = remember { mutableStateOf<List<Any>>(emptyList()) }
    // リストを読み込む部分(後述）
    LazyColumn(
    ) {
        items(ary.value) { obj ->
            ListRow(obj)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListScreenPreview() {
    ListScreen()
}
