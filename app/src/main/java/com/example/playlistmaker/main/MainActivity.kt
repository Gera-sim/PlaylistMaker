package com.example.playlistmaker.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.playlistmaker.medialibrary.ui.MediaLibActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.ui.SettingsActivity
import com.example.playlistmaker.search.ui.SearchActivity

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