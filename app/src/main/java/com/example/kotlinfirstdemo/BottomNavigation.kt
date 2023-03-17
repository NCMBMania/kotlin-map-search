package com.example.kotlinfirstdemo

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddBox
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kotlinfirstdemo.ui.theme.KotlinFirstDemoTheme
import androidx.navigation.compose.NavHost as NavHost

sealed class Item(var dist: String, var icon: ImageVector) {
    // 地図画面用
    object Map : Item("Map", Icons.Rounded.Map)
    // インポート画面用
    object Import : Item("Import", Icons.Rounded.Settings)
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun MapBottomNavigation() {
    var selectedItem = remember { mutableStateOf(0) }
    val items = listOf(Item.Map, Item.Import)
    val navController = rememberNavController()
    KotlinFirstDemoTheme {
        Surface(color = MaterialTheme.colors.background) {
            Scaffold(
                bottomBar = {
                    BottomNavigation {
                        items.forEachIndexed { index, item ->
                            BottomNavigationItem(
                                icon = { Icon(item.icon, contentDescription = item.dist) },
                                label = { Text(item.dist) },
                                selected = selectedItem.value == index,
                                onClick = {
                                    navController.navigate(item.dist)
                                }
                            )
                        }
                    }
                }
            ) { _ ->
                NavHost(navController = navController, startDestination = "Map") {
                    composable("Map") { MapScreen()}
                    composable("Import") { ImportScreen()}
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Preview(showBackground = true)
@Composable
fun BottomNavigationPreview() {
    KotlinFirstDemoTheme {
        MapBottomNavigation()
    }
}
