package com.mhelrigo.cocktailmanual.ui.commons.base

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.window.SplashScreen
import androidx.activity.OnBackPressedCallback
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.mhelrigo.cocktailmanual.R
import com.mhelrigo.cocktailmanual.di.AppModule.IS_TABLET
import com.mhelrigo.cocktailmanual.ui.settings.SettingsViewModel
import com.mhelrigo.commons.DISPATCHERS_IO
import com.mhelrigo.commons.DISPATCHERS_MAIN
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment<T : ViewBinding> : Fragment(), RetryRequestManager {
    protected lateinit var binding: T

    abstract fun inflateLayout(@NonNull inflater: LayoutInflater): T

    @JvmField
    @Inject
    @Named(IS_TABLET)
    var isTablet: Boolean? = null

    @Inject
    @Named(DISPATCHERS_MAIN)
    lateinit var mainCoroutine: CoroutineContext

    @Inject
    @Named(DISPATCHERS_IO)
    lateinit var ioCoroutineContext: CoroutineContext

    private val settingsViewModel: SettingsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflateLayout(inflater)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpDeviceBackNavigation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleInternetConnectivityChanges()
    }

    open fun processLoadingState(isLoad: Boolean, view: View?) {
        val objectAnimator: ValueAnimator =
            ObjectAnimator.ofFloat(view, View.ROTATION, -360f, 0f)
        objectAnimator.duration = 2500
        objectAnimator.repeatCount = ValueAnimator.INFINITE
        if (isLoad) {
            objectAnimator.start()
        } else {
            objectAnimator.cancel()
        }
    }

    open fun setUpDeviceBackNavigation() {
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (!findNavController().popBackStack()) {
                        requireActivity().finish()
                    }
                }
            })
    }

    fun navigateWithBundle(bundle: Bundle, action: Int) {
        findNavController().navigate(
            action,
            bundle
        )
    }

    private fun handleInternetConnectivityChanges() {
        settingsViewModel.isNetworkAvailable
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach {
                if (it) {
                    requestData()
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
}