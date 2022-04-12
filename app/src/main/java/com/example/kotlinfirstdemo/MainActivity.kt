package com.example.kotlinfirstdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.kotlinfirstdemo.ui.theme.KotlinFirstDemoTheme
import com.nifcloud.mbaas.core.NCMB

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NCMB.initialize(
            this.getApplicationContext(),
            "af464eb21c4723c258b3d0dec841ce960ae313e846e9b081cdbdb2a2d38d4ccf",
            "18e6b393060588dbfb88363396078fea0843a3324bc9b69cbc86d7d34ec2e9fd"
        )
        setContent {
            MemoBottomNavigation()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KotlinFirstDemoTheme {
        MemoBottomNavigation()
    }
}
