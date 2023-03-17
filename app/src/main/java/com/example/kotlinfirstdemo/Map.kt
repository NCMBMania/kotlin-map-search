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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.rounded.Image
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptorFactory
// import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
// import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.nifcloud.mbaas.core.NCMBGeoPoint
import com.nifcloud.mbaas.core.NCMBObject
import com.nifcloud.mbaas.core.NCMBQuery
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun MapScreen() {
    val context = LocalContext.current
    // タップした位置情報が入る
    var markers = remember { mutableStateListOf<LatLng>() }
    // 検索条件にマッチする駅一覧が入る
    var stations = remember { mutableStateListOf<NCMBObject>() }
    // 画面について
    Scaffold(
        // フローティングアイコンボタン
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(end = 50.dp, bottom = 50.dp),
                onClick = {
                    // 選択されたマーカー、駅情報をクリアする
                    markers.clear()
                    stations.clear()
                }) {
                Icon(Icons.Filled.Delete, contentDescription = "削除")
            }
        }
    ) { padding ->
        // 初期表示の位置情報（東京タワー）
        val tokyo = LatLng(35.6585805, 139.7454329)
        // 初期表示のズーム設定
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(tokyo, 13f)
        }
        // Googleマップのコンポーネント
        GoogleMap(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { position ->
                // 駅情報はクリア
                stations.clear()
                if (markers.size == 0) {
                    // なければ追加するだけ
                    markers.add(position)
                } else {
                    // 選択された位置情報を最初に追加
                    markers.add(0, position)
                }
                // 位置情報が2つ以上なら、最後のは不要
                if (markers.size > 2) {
                    markers.removeAt(2)
                }
                // 選択されている位置情報の数に応じて検索方法を変更
                val ary = if (markers.size == 1)
                    searchStationsNear(position) // 位置情報1つなら付近の駅を検索
                    else searchStationSquare(markers) // 位置情報が2つなら矩形検索
                ary.forEach {
                    stations.add(it)
                }
            }
        ) {
            // タップした箇所を表示するマーカー
            for(marker in markers) {
                Marker(
                    state = MarkerState(position = marker),
                    title = "marker",
                )
            }

            // 駅を青いマーカーで表示
            for (station in stations) {
                val geo = station.getGeo("geo")
                Marker(
                    // 位置情報を取り出す
                    state = MarkerState(position = LatLng(geo.mlatitude, geo.mlongitude)),
                    title = "${station.getString("name")}駅",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                )
            }
        }
    }
}

// 1つのマーカーの付近（3.0km）を検索する関数
fun searchStationsNear(position: LatLng): List<NCMBObject> {
    // 検索するNCMBのデータストアクラス
    val query = NCMBQuery.forObject("Station")
    // 位置情報をNCMBGeoPointに変換
    val geo = NCMBGeoPoint(latitude = position.latitude, longitude = position.longitude)
    query.whereNearSphereKilometers("geo", geo, 3.0)
    return query.find()
}

// 2つのマーカーに挟まれた駅を検索する関数
fun searchStationSquare(positions: List<LatLng>): List<NCMBObject> {
    // 検索するNCMBのデータストアクラス
    val query = NCMBQuery.forObject("Station")
    // 位置情報をNCMBGeoPointに変換
    val geo1 = NCMBGeoPoint(latitude = positions[0].latitude, longitude = positions[0].longitude)
    val geo2 = NCMBGeoPoint(latitude = positions[1].latitude, longitude = positions[1].longitude)
    // 検索条件に指定
    query.whereWithinGeoBox("geo", geo1, geo2)
    // 検索実行
    return query.find()
}