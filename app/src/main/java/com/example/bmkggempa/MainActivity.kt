package com.example.bmkggempa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bmkggempa.data.model.Gempa
import com.example.bmkggempa.ui.GempaUiState
import com.example.bmkggempa.ui.GempaViewModel
import com.example.bmkggempa.ui.theme.BmkgGempaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { BmkgGempaTheme { GempaApp() } }
    }
}

@Composable
private fun GempaApp(viewModel: GempaViewModel = viewModel(factory = GempaViewModel.factory())) {
    val navController = rememberNavController()
    val state by viewModel.uiState.collectAsState()
    var selectedGempa by remember { mutableStateOf<Gempa?>(null) }

    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(state, viewModel::loadGempa) { gempa ->
                selectedGempa = gempa
                navController.navigate("detail")
            }
        }
        composable("detail") {
            selectedGempa?.let { DetailScreen(it, navController) }
                ?: HomeScreen(state, viewModel::loadGempa) { gempa -> selectedGempa = gempa; navController.navigate("detail") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(state: GempaUiState, onRetry: () -> Unit, onGempaClick: (Gempa) -> Unit) {
    var query by remember { mutableStateOf("") }
    Scaffold(topBar = { TopAppBar(title = { Text("Katalog Gempa BMKG") }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                label = { Text("Cari wilayah gempa") },
                singleLine = true
            )
            when (state) {
                GempaUiState.Loading -> LoadingContent()
                is GempaUiState.Error -> ErrorContent(state.message, onRetry)
                is GempaUiState.Success -> {
                    val filtered = state.items.filter { it.wilayah.contains(query, ignoreCase = true) }
                    if (filtered.isEmpty()) EmptyContent()
                    else GempaList(filtered, onGempaClick)
                }
            }
        }
    }
}

@Composable
private fun GempaList(items: List<Gempa>, onGempaClick: (Gempa) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(bottom = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items, key = { "${it.tanggal}-${it.jam}-${it.wilayah}" }) { gempa ->
            GempaItem(gempa, Modifier.clickable { onGempaClick(gempa) })
        }
    }
}

@Composable
private fun GempaItem(gempa: Gempa, modifier: Modifier = Modifier) {
    Card(modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(gempa.tanggal, style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.weight(1f))
                Text("M ${gempa.magnitude}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            Text(gempa.wilayah, modifier = Modifier.padding(top = 8.dp), maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailScreen(gempa: Gempa, navController: NavHostController) {
    Scaffold(topBar = { TopAppBar(title = { Text("Detail Gempa") }, navigationIcon = {
        Text("‹", modifier = Modifier.padding(start = 16.dp).clickable { navController.popBackStack() }, style = MaterialTheme.typography.headlineMedium)
    }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            item { DetailRow("Tanggal", gempa.tanggal) }
            item { DetailRow("Jam", gempa.jam) }
            item { DetailRow("Coordinates", gempa.coordinates) }
            item { DetailRow("Magnitudo", gempa.magnitude) }
            item { DetailRow("Kedalaman", gempa.kedalaman) }
            item { DetailRow("Wilayah", gempa.wilayah) }
            item { DetailRow("Potensi", gempa.potensi) }
        }
    }
}

@Composable private fun DetailRow(label: String, value: String) {
    Column(Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
    HorizontalDivider()
}

@Composable private fun LoadingContent() = Column(Modifier.fillMaxWidth().padding(top = 48.dp), horizontalAlignment = Alignment.CenterHorizontally) { CircularProgressIndicator(); Text("Memuat data gempa…", Modifier.padding(top = 12.dp)) }
@Composable private fun EmptyContent() = Column(Modifier.fillMaxWidth().padding(top = 48.dp), horizontalAlignment = Alignment.CenterHorizontally) { Text("Tidak ada wilayah yang cocok") }
@Composable private fun ErrorContent(message: String, onRetry: () -> Unit) = Column(Modifier.fillMaxWidth().padding(top = 48.dp), horizontalAlignment = Alignment.CenterHorizontally) { Text("Terjadi kesalahan: $message"); Button(onClick = onRetry, modifier = Modifier.padding(top = 12.dp)) { Text("Coba lagi") } }
