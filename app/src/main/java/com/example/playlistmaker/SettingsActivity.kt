package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val homeButton = findViewById<Button>(R.id.Home)
        homeButton.setOnClickListener {
            val displayIntent = Intent(this, MainActivity::class.java)
            startActivity(displayIntent)
        }

        val shareButton = findViewById<LinearLayout>(R.id.Share)
        shareButton.setOnClickListener {
            val androidDevelopment = getString(R.string.android_developement_course)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = Uri.parse("text/plain").toString()
            shareIntent.putExtra(Intent.EXTRA_TEXT, androidDevelopment)
            val chooserIntent = Intent.createChooser(shareIntent, null)
            startActivity(chooserIntent)
        }

        val supportButton = findViewById<LinearLayout>(R.id.Support)
        supportButton.setOnClickListener{

        }

        val agreementButton = findViewById<LinearLayout>(R.id.Agreement)
        agreementButton.setOnClickListener {

        }


    }
}