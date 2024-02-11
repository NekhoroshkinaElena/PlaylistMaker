package com.example.playlistmaker.presentation.setting

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED) {
            binding.themeSwitch.isChecked = (resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        } else {
            binding.themeSwitch.isChecked =
                AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        }

        binding.themeSwitch.setOnClickListener {
            (applicationContext as App).switchTheme(binding.themeSwitch.isChecked)
            recreate()
        }

        binding.toolbarSettings.setNavigationOnClickListener {
            finish()
        }

        binding.shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain" +
                    shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.android_developer))
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share)))
        }

        binding.supportButton.setOnClickListener {
            val message = getString(R.string.message_for_developer)
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.your_email)))
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            shareIntent.putExtra(
                Intent.EXTRA_SUBJECT, getString(R.string.subject_message)
            )
            startActivity(shareIntent)
        }

        binding.agreementButton.setOnClickListener {
            val agreementIntent = Intent(Intent.ACTION_VIEW)
            agreementIntent.data = Uri.parse(getString(R.string.practicum_offer))
            startActivity(agreementIntent)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (!(applicationContext as App).hasRecordInSharedPreferences()) {
            binding.themeSwitch.isChecked = (newConfig.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            recreate()
        }
    }
}
