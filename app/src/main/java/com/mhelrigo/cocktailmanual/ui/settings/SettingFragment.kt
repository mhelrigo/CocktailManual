package com.mhelrigo.cocktailmanual.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import com.mhelrigo.cocktailmanual.BuildConfig
import com.mhelrigo.cocktailmanual.databinding.FragmentSettingBinding
import com.mhelrigo.cocktailmanual.ui.commons.base.BaseFragment

class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    private val settingsViewModel: SettingsViewModel by activityViewModels()

    override fun inflateLayout(inflater: LayoutInflater): FragmentSettingBinding =
        FragmentSettingBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchNightMode.setOnCheckedChangeListener { _, p1 ->
            settingsViewModel.toggleNightMode(p1)
        }

        settingsViewModel.isNightMode.observe(viewLifecycleOwner, {
            it?.let {
                binding.switchNightMode.isChecked = it
            }
        })

        displayApplicationVersion()
    }

    private fun displayApplicationVersion() {
        binding.textViewVersion.text = "version: ${BuildConfig.VERSION_NAME}"
    }
}