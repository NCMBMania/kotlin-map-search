package com.example.kotlinfirstdemo

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.kotlinfirstdemo.ui.theme.KotlinFirstDemoTheme
import com.nifcloud.mbaas.core.NCMB

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // NCMBの初期化
        NCMB.initialize(
            this.getApplicationContext(),
            "9170ffcb91da1bbe0eff808a967e12ce081ae9e3262ad3e5c3cac0d9e54ad941",
            "9e5014cd2d76a73b4596deffdc6ec4028cfc1373529325f8e71b7a6ed553157d"
        )
        setContent {
            MapBottomNavigation()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KotlinFirstDemoTheme {
        MapBottomNavigation()
    }
}
