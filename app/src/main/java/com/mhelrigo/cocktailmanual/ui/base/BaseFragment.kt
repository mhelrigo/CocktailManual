package com.mhelrigo.cocktailmanual.ui.base

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.mhelrigo.cocktailmanual.ui.setUpDeviceBackNavigation

abstract class BaseFragment<T : ViewBinding> : Fragment() {
    protected lateinit var binding: T

    abstract fun inflateLayout(@NonNull inflater: LayoutInflater): T

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
}