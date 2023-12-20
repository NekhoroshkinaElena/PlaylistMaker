package com.example.playlistmaker

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.darkTheme)

        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            (applicationContext as App).switchTheme(isChecked)
        }

        themeSwitcher.isChecked = (resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

        val toolbar = findViewById<Toolbar>(R.id.toolbar_settings)
        toolbar.setNavigationOnClickListener() {
            finish()
        }

        val shareButton = findViewById<Button>(R.id.share)
        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain" +
                    shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.android_developer))
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share)))
        }

        val supportButton = findViewById<Button>(R.id.support)
        supportButton.setOnClickListener {
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

        val agreementButton = findViewById<Button>(R.id.user_agreement)
        agreementButton.setOnClickListener {
            val agreementIntent = Intent(Intent.ACTION_VIEW)
            agreementIntent.data = Uri.parse(getString(R.string.practicum_offer))
            startActivity(agreementIntent)
        }
    }
}
