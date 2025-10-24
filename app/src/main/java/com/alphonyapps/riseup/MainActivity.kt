package com.alphonyapps.riseup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.alphonyapps.riseup.ui.theme.RiseUpTheme
import org.json.JSONArray
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val quotes = readQuotes(this)
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val quoteOfTheDay = quotes[dayOfYear % quotes.size]

        setContent {
            RiseUpTheme {
                RiseUpApp(quoteOfTheDay.first, quoteOfTheDay.second)
            }
        }
    }

    fun readQuotes(context: Context): List<Pair<String, String>> {
        val inputStream = context.resources.openRawResource(R.raw.quotes)
        val json = inputStream.bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(json)
        val list = mutableListOf<Pair<String, String>>()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val quote = obj.getString("quote")
            val author = obj.getString("author")
            list.add(Pair(quote, author))
        }
        return list
    }
}

@Composable
fun RiseUpApp(quote: String, author: String) {
    val colors = listOf(Color(0xFF6DD5FA), Color(0xFFB993D6), Color(0xFFFFD194))
    val icons = listOf("🌞", "🌱", "💪", "🚀", "✨", "🎉", "💖", "🌟", "🔥", "💡")
    val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
    val backgroundColor = colors[dayOfYear % colors.size]
    val dailyIcon = icons[dayOfYear % icons.size]
    val context = LocalContext.current
    var visible by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(getFavorites(context).contains(quote to author)) }

    LaunchedEffect(Unit) {
        visible = true
    }

    Scaffold(
        floatingActionButton = {
            Column {
                FloatingActionButton(
                    onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "\"$quote\" - $author")
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "Share via"))
                    },
                    containerColor = Color(0xFFf57c00)
                ) {
                    Icon(Icons.Default.Share, contentDescription = "Share")
                }
                FloatingActionButton(
                    onClick = { context.startActivity(Intent(context, FavoritesActivity::class.java)) },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = "Favorites")
                }
            }
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize().background(backgroundColor).padding(it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(visible = visible) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = dailyIcon,
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(
                                    text = "\"$quote\"",
                                    style = MaterialTheme.typography.headlineSmall,
                                )
                            }
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "- $author",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                                IconButton(onClick = {
                                    if (isFavorite) {
                                        removeFavorite(context, quote to author)
                                    } else {
                                        saveFavorite(context, quote to author)
                                    }
                                    isFavorite = !isFavorite
                                }) {
                                    Icon(
                                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "Save Favorite"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
