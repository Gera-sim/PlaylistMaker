package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.Main_Search)
        buttonSearch.setOnClickListener {
            val displayIntent = Intent(this, Search_activity::class.java)
            startActivity(displayIntent)
        }


        val buttonMediaLibrary = findViewById<Button>(R.id.Main_Media)
        buttonMediaLibrary.setOnClickListener {
            val displayIntent = Intent(this, MediaLib_activity::class.java)
            startActivity(displayIntent)
        }

        val buttonMainSettings = findViewById<Button>(R.id.Main_Settings)
        buttonMainSettings.setOnClickListener {
            val displayIntent = Intent(this, Settings_activity::class.java)
            startActivity(displayIntent)
        }


    }
}