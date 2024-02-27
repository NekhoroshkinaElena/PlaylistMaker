package com.example.playlistmaker.settings.ui.activity

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.App
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.ui.models.ThemeState
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]

        viewModel.getThemeSettings()

        viewModel.getStateThemeApp().observe(this) { render(it) }

        binding.themeSwitch.setOnClickListener {
            viewModel.updateTheme(binding.themeSwitch.isChecked)
            recreate()
        }

        binding.toolbarSettings.setNavigationOnClickListener {
            finish()
        }

        binding.shareButton.setOnClickListener {
            viewModel.shareApp()
        }

        binding.supportButton.setOnClickListener {
            viewModel.openSupport()
        }

        binding.agreementButton.setOnClickListener {
            viewModel.openTerms()
        }
    }

    private fun render(themeState: ThemeState) {
        when (themeState) {
            is ThemeState.SystemTheme -> {
                binding.themeSwitch.isChecked = themeState.isChecked
            }

            is ThemeState.LightTheme -> {
                binding.themeSwitch.isChecked = false
            }

            is ThemeState.DarkTheme -> {
                binding.themeSwitch.isChecked = true
            }
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
