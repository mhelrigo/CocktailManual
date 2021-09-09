package com.mhelrigo.cocktailmanual.ui

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

internal fun Fragment.setUpDeviceBackNavigation() {
    requireActivity().onBackPressedDispatcher.addCallback(
        this,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })
}

internal fun Fragment.navigateWithBundle(bundle: Bundle, action: Int) {
    findNavController().navigate(
        action,
        bundle
    )
}