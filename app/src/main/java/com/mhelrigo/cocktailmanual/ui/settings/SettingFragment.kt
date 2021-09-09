package com.mhelrigo.cocktailmanual.ui.settings

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mhelrigo.cocktailmanual.BuildConfig
import com.mhelrigo.cocktailmanual.databinding.FragmentSettingBinding
import com.mhelrigo.cocktailmanual.ui.setUpDeviceBackNavigation

class SettingFragment : Fragment() {
    private lateinit var settingBinding: FragmentSettingBinding

    private val settingsViewModel: SettingsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingBinding = FragmentSettingBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return settingBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpDeviceBackNavigation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingBinding.switchNightMode.setOnCheckedChangeListener { _, p1 ->
            settingsViewModel.toggleNightMode(p1)
        }

        settingsViewModel.toggleNightMode(isNightMode(resources.configuration))
        settingsViewModel.isNightMode.observe(viewLifecycleOwner, {
            it?.let {
                settingBinding.switchNightMode.isChecked = it
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
        settingBinding.textViewVersion.text = "version: ${BuildConfig.VERSION_NAME}"
    }
}