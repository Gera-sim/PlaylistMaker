package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.main_search)
        buttonSearch.setOnClickListener {
            val displayIntent = Intent(this, SearchActivity::class.java)
            startActivity(displayIntent)
        }


        val buttonMediaLibrary = findViewById<Button>(R.id.Main_Media)
        buttonMediaLibrary.setOnClickListener {
            val displayIntent = Intent(this, MediaLibActivity::class.java)
            startActivity(displayIntent)
        }

        val buttonMainSettings = findViewById<Button>(R.id.Main_Settings)
        buttonMainSettings.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }


    }
}