package com.example.playlistmaker.presentation.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.mediaLib.MediaLibActivity
import com.example.playlistmaker.presentation.settings.SettingsActivity
import com.example.playlistmaker.presentation.search.SearchActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.Main_search).setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
        findViewById<Button>(R.id.Main_Media).setOnClickListener {
            startActivity(Intent(this, MediaLibActivity::class.java))
        }
        findViewById<Button>(R.id.Main_Settings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}