package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.data.AppDatabase
import com.example.data.SavingsRepository
import com.example.ui.HomeScreen
import com.example.ui.SavingsViewModel
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "savings-db"
        ).build()
        val repository = SavingsRepository(db.savingsDao())

        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: SavingsViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return SavingsViewModel(repository) as T
                        }
                    }
                )
                Surface {
                    HomeScreen(viewModel = viewModel)
                }
            }
        }
    }
}
