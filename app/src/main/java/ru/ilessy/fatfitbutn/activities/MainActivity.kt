package ru.ilessy.fatfitbutn.activities

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import ru.ilessy.fatfitbutn.R
import ru.ilessy.fatfitbutn.databinding.ActivityMainBinding
import ru.ilessy.fatfitbutn.fragments.video.VideoWorkoutFragment
import ru.ilessy.fatfitbutn.fragments.workout.WorkoutFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).root)
        openWorkoutFragment()
        observeVideoLiveData()
        if (isVideoWorkoutFragment()) {
            goToImmersiveMode()
        }
    }


    @SuppressLint("CommitTransaction")
    private fun openWorkoutFragment() {
        val fragments = supportFragmentManager.fragments
        if (fragments.isEmpty()) {
            val workoutFragment = WorkoutFragment()
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                workoutFragment
            ).commit()
        }
    }

    @SuppressLint("CommitTransaction")
    private fun observeVideoLiveData() {
        mainViewModel.videoLiveData.observe(this) { videoWorkout ->
            if (mainViewModel.isValidVideoWorkout(videoWorkout = videoWorkout)) {
                mainViewModel.updateVideoWorkout(videoWorkout = videoWorkout)
                if (!isVideoWorkoutFragment()) {
                    val videoWorkoutFragment = VideoWorkoutFragment()
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_container,
                        videoWorkoutFragment
                    ).addToBackStack(null).commit().apply {
                        goToImmersiveMode()
                    }
                }
            }
        }
    }

    private fun isVideoWorkoutFragment(): Boolean {
        return supportFragmentManager.findFragmentById(R.id.fragment_container) is VideoWorkoutFragment
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        if (isVideoWorkoutFragment()) {
            exitImmersiveMode()
        }
        super.onBackPressed()
    }

    @Suppress("DEPRECATION")
    private fun goToImmersiveMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController.apply {
                this?.hide(WindowInsets.Type.systemBars())
                this?.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            val decorView = window.decorView
            decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    )
        }
    }

    @Suppress("DEPRECATION")
    private fun exitImmersiveMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.show(WindowInsets.Type.systemBars())
        } else {
            val decorView = window.decorView
            decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    )
        }
    }

}