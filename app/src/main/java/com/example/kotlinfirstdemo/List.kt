package com.example.kotlinfirstdemo

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
/*
import com.nifcloud.mbaas.core.NCMBCallback
import com.nifcloud.mbaas.core.NCMBObject
import com.nifcloud.mbaas.core.NCMBQuery

*/

@Composable
fun ListScreen() {
    // ↓ エラーにならないためのダミー
    var ary = remember { mutableStateOf<List<Any>>(emptyList()) }
    // リストを読み込む部分(後述）
    /*
    var ary = remember { mutableStateOf<List<NCMBObject>>(emptyList()) }
    // Memoクラスを検索するクエリー
    val query = NCMBQuery.forObject("Memo")
    query.findInBackground(NCMBCallback { e, results ->
        if (e == null) {
            // 結果をaryに適用
            ary.value = results as List<NCMBObject>
        }
    })
    */
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
