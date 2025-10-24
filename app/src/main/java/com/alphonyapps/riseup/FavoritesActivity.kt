package com.alphonyapps.riseup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.alphonyapps.riseup.ui.theme.RiseUpTheme

class FavoritesActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RiseUpTheme {
                var favorites by remember { mutableStateOf(getFavorites(this)) }
                Scaffold(
                    topBar = {
                        TopAppBar(title = { Text("Favorites") })
                    }
                ) {
                    FavoritesScreen(favorites, Modifier.padding(it)) {
                        favorites = getFavorites(this)
                    }
                }
            }
        }
    }
}

@Composable
fun FavoritesScreen(
    favorites: List<Pair<String, String>>,
    modifier: Modifier = Modifier,
    onFavoriteRemoved: () -> Unit
) {
    val context = LocalContext.current
    LazyColumn(modifier = modifier) {
        items(favorites) { (quote, author) ->
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "\"$quote\"",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = "- $author",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    IconButton(onClick = {
                        removeFavorite(context, quote to author)
                        onFavoriteRemoved()
                    }) {
                        Icon(Icons.Default.Favorite, contentDescription = "Remove Favorite")
                    }
                }
            }
        }
    }
}
