package ru.ilessy.fatfitbutn.activities

import android.annotation.SuppressLint
import android.os.Bundle
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
            videoWorkout?.let {
                if (supportFragmentManager.findFragmentById(R.id.fragment_container) !is VideoWorkoutFragment) {
                    val videoWorkoutFragment = VideoWorkoutFragment()
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_container,
                        videoWorkoutFragment
                    ).addToBackStack(null).commit()
                }
            } ?: run {
                //TODO добавить обработку
            }
        }
    }

}