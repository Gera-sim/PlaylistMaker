package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.Main_Search)
        buttonSearch.setOnClickListener {  Toast.makeText(this@MainActivity, "Нажали на кнопку поиск!", Toast.LENGTH_SHORT).show()  }


        val buttonMediaLibrary = findViewById<Button>(R.id.Main_Media)

        val buttonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Клацнули кнопку медиатека!", Toast.LENGTH_LONG).show()
            }
        }
        buttonMediaLibrary.setOnClickListener(buttonClickListener)

        val buttonMainSettings = findViewById<Button>(R.id.Main_Settings)
        buttonMainSettings.setOnClickListener {
            Toast.makeText(this@MainActivity,"Гаечный ключ и пассатижи у бухгалтера :)", Toast.LENGTH_SHORT
            ).show()
        }


    }
}
