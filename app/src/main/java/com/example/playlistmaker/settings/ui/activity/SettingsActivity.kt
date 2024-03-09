package com.example.playlistmaker.settings.ui.activity

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
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

        binding.toolbarSettings.setNavigationOnClickListener {
            finish()
        }

        viewModel.renderCurrentTheme()

        viewModel.getStateThemeApp().observe(this) {
            render(it)
        }

        binding.themeSwitch.setOnCheckedChangeListener { button, isChecked ->
            if (button.isPressed) {
                viewModel.updateTheme(isChecked)
            }
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
        recreate()
    }
}
