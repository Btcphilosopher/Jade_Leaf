package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.data.database.TeaDatabase
import com.example.data.repository.TeaRepository
import com.example.ui.screens.MainScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.TeaViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var database: TeaDatabase
    private lateinit var repository: TeaRepository
    private lateinit var viewModel: TeaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Initialize Room Database and Repository
        database = TeaDatabase.getDatabase(applicationContext)
        repository = TeaRepository(database.teaDao())

        // 2. Pre-populate database with default items on a background thread
        lifecycleScope.launch {
            repository.prepopulateDatabaseIfEmpty()
        }

        // 3. Create ViewModel via Factory
        val factory = TeaViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[TeaViewModel::class.java]

        // 4. Set Content
        setContent {
            MyApplicationTheme {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}

// Standard ViewModelProvider Factory for clean Constructor Injection
class TeaViewModelFactory(private val repository: TeaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TeaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TeaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
