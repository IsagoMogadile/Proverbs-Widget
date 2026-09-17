package com.proverbs.widget

import android.content.Context
import org.json.JSONArray
import kotlin.random.Random

data class Verse(val chapter: Int, val verse: Int, val text: String)

object VerseRepository {

    private var cache: List<Verse>? = null

    private fun loadVerses(context: Context): List<Verse> {
        cache?.let { return it }
        val json = context.assets.open("proverbs_data.json").bufferedReader().use { it.readText() }
        val array = JSONArray(json)
        val verses = ArrayList<Verse>(array.length())
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            verses.add(Verse(obj.getInt("chapter"), obj.getInt("verse"), obj.getString("text")))
        }
        cache = verses
        return verses
    }

    fun randomVerse(context: Context): Verse {
        val verses = loadVerses(context)
        return verses[Random.nextInt(verses.size)]
    }
}
