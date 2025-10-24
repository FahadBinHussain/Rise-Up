package com.alphonyapps.riseup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.alphonyapps.riseup.ui.theme.Purple40
import com.alphonyapps.riseup.ui.theme.Purple80
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
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Purple80, Purple40)
    )
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBrush)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
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
            }

            Button(
                onClick = {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "\"$quote\" - $author")
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "Share via"))
                },
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Text("Share")
            }
        }
    }
}
