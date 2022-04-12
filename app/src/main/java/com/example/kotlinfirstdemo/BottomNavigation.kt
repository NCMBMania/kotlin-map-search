package com.example.kotlinfirstdemo

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddBox
import androidx.compose.material.icons.rounded.List
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
    object Form : Item("Form", Icons.Rounded.AddBox)
    object List : Item("List", Icons.Rounded.List)
}

@Composable
fun MemoBottomNavigation() {
    var selectedItem = remember { mutableStateOf(0) }
    val items = listOf(Item.Form, Item.List)
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
            ) {
                NavHost(navController = navController, startDestination = "Form") {
                    composable("Form") { FormScreen()}
                    composable("List") { ListScreen()}
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationPreview() {
    KotlinFirstDemoTheme {
        MemoBottomNavigation()
    }
}
