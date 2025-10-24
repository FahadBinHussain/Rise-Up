package com.alphonyapps.riseup

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun saveFavorite(context: Context, quote: Pair<String, String>) {
    val sharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
    val gson = Gson()
    val favorites = getFavorites(context).toMutableList()
    if (!favorites.contains(quote)) {
        favorites.add(quote)
        val editor = sharedPreferences.edit()
        editor.putString("quotes", gson.toJson(favorites))
        editor.apply()
    }
}

fun removeFavorite(context: Context, quote: Pair<String, String>) {
    val sharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
    val gson = Gson()
    val favorites = getFavorites(context).toMutableList()
    if (favorites.contains(quote)) {
        favorites.remove(quote)
        val editor = sharedPreferences.edit()
        editor.putString("quotes", gson.toJson(favorites))
        editor.apply()
    }
}

fun getFavorites(context: Context): List<Pair<String, String>> {
    val sharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
    val gson = Gson()
    val json = sharedPreferences.getString("quotes", "[]")
    val type = object : TypeToken<List<Pair<String, String>>>() {}.type
    return gson.fromJson(json, type)
}
