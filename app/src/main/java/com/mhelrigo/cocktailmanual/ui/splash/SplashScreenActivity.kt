package com.mhelrigo.cocktailmanual.ui.splash

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.mhelrigo.cocktailmanual.R
import com.mhelrigo.cocktailmanual.databinding.FragmentSplashScreenBinding
import com.mhelrigo.cocktailmanual.ui.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

private const val ANIMATION_DURATION_MILLIS = 5_00L

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: FragmentSplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animateIcon()
    }

    private fun animateIcon() {
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.8F)
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.8F)
        val animator = ObjectAnimator.ofPropertyValuesHolder(binding.imageViewIcon, scaleY, scaleX)

        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {
            }

            override fun onAnimationEnd(p0: Animator?) {
                startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java))
                finish()
            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationRepeat(p0: Animator?) {
            }
        })

        animator.duration = ANIMATION_DURATION_MILLIS
        animator.start()
    }
}