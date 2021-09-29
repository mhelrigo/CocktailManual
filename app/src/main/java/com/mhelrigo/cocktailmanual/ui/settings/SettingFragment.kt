package com.mhelrigo.cocktailmanual.ui.settings

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import com.mhelrigo.cocktailmanual.BuildConfig
import com.mhelrigo.cocktailmanual.databinding.FragmentSettingBinding
import com.mhelrigo.cocktailmanual.ui.base.BaseFragment

class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    private val settingsViewModel: SettingsViewModel by activityViewModels()

    override fun inflateLayout(inflater: LayoutInflater): FragmentSettingBinding =
        FragmentSettingBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchNightMode.setOnCheckedChangeListener { _, p1 ->
            settingsViewModel.toggleNightMode(p1)
        }

        settingsViewModel.toggleNightMode(isNightMode(resources.configuration))
        settingsViewModel.isNightMode.observe(viewLifecycleOwner, {
            it?.let {
                binding.switchNightMode.isChecked = it
            }
        })

        displayApplicationVersion()
    }

    /**
     * Method that check if the system is in Dark Mode
     * */
    private fun isNightMode(newConfig: Configuration): Boolean {
        return when (newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
    }

    private fun displayApplicationVersion() {
        binding.textViewVersion.text = "version: ${BuildConfig.VERSION_NAME}"
    }
}