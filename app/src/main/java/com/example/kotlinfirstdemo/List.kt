package com.example.kotlinfirstdemo

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.nifcloud.mbaas.core.NCMBCallback
import com.nifcloud.mbaas.core.NCMBObject
import com.nifcloud.mbaas.core.NCMBQuery

@Composable
fun ListScreen(ary: List<NCMBObject> = listOf<NCMBObject>()) {
    var ary = remember { mutableStateOf<List<NCMBObject>>(emptyList()) }
    val query = NCMBQuery.forObject("Memo")
    query.findInBackground(NCMBCallback { e, results ->
        if (e == null) {
            ary.value = results as List<NCMBObject>
        }
    })
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
    var obj = NCMBObject("Memo");
    obj.put("memo", "Test")
    ListScreen(listOf(obj))
}
