package com.example.kotlinfirstdemo

import android.content.res.AssetManager
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.async
import androidx.compose.ui.platform.LocalContext
// import androidx.compose.ui.tooling.data.EmptyGroup.name
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nifcloud.mbaas.core.NCMBCallback
import com.nifcloud.mbaas.core.NCMBGeoPoint
import com.nifcloud.mbaas.core.NCMBObject
import com.nifcloud.mbaas.core.NCMBQuery
import kotlinx.coroutines.Dispatchers
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader


@Composable
fun ImportScreen() {
    // ログメッセージ用
    var logs = remember { mutableStateListOf<String>() }

    // 山手線のJSONデータを読み込む処理
    val context = LocalContext.current
    var inputStream = context.assets.open("yamanote.json")
    val json = BufferedReader(InputStreamReader(inputStream)).readText()
    val stationsJson = JSONArray(json)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.size(20.dp))
        Button(
            onClick = {
                // 既存データの削除
                deleteAllStations()
                // JSONに入っている駅情報をNCMBのデータストアに保存
                for (i in 0 until stationsJson.length()) {
                    val str = saveAllStations(stationsJson.getJSONObject(i))
                    logs.add(str)
                }
            }
        ){
            Text(text = "山手線データをインポートする")
        }
        LazyColumn(
        ) {
            items(logs) { str ->
                Text(text = str)
            }
        }
    }
}

// 既存のデータを削除する処理
fun deleteAllStations() {
    // 検索対象のNCMBのデータストアクラス
    val query = NCMBQuery.forObject("Station")
    query.limit = 1000 // マックスまで指定しておく
    // 検索は同期で
    val stations = query.find()
    // 削除は非同期で終わらせる
    stations.forEach{
        it.deleteInBackground(NCMBCallback { e, _ ->
        })
    }
}

// 駅情報を保存する処理
fun saveAllStations(json: JSONObject ): String {
    // 位置情報からNCMBGeoPointを作成
    val geo = NCMBGeoPoint(latitude = json.getDouble("latitude"), longitude = json.getDouble("longitude"))
    // データストアのインスタンスを作成
    val station = NCMBObject("Station")
    // 駅名と位置情報をセット
    station.put("geo", geo)
    station.put("name", json.getString("name"))
    // 保存（量が多いので非同期で）
    station.saveInBackground(NCMBCallback {e, _ ->

    })
    return "${station.getString("name")}を取り込みました"
}

@Preview(showBackground = true)
@Composable
fun ImportScreenPreview() {
    ImportScreen()
}
