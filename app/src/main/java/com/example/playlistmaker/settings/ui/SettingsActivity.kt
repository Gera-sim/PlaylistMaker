package com.example.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.Home.setOnClickListener { finish() }

        binding.themeSwitcher.apply {
            isChecked = viewModel.isDarkThemeOn()
            setOnCheckedChangeListener { _, isChecked ->
                viewModel.switchTheme(isChecked)
            }
        }

        binding.Share.setOnClickListener {
            val androidDevelopment = getString(R.string.android_development_course)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = Uri.parse("text/plain").toString()
            shareIntent.putExtra(Intent.EXTRA_TEXT, androidDevelopment)
            startActivity(intent)
        }

        binding.Support.setOnClickListener {
            val callSupport = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                //т.к. приложение в открытом доступе, личными данными не сорим
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.supportMail)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mailSubject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.supportMessage))
            }
            startActivity(callSupport)
        }

        binding.Agreement.setOnClickListener {
            val openPage = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.agreement)))
            startActivity(openPage)
        }
    }
}