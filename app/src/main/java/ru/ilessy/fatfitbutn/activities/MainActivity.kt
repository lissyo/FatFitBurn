package ru.ilessy.fatfitbutn.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import ru.ilessy.fatfitbutn.R
import ru.ilessy.fatfitbutn.databinding.ActivityMainBinding
import ru.ilessy.fatfitbutn.fragments.workout.WorkoutFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).root)
        openFragment()
    }


    @SuppressLint("CommitTransaction")
    private fun openFragment() {
        val fragments = supportFragmentManager.fragments
        if (fragments.isEmpty()) {
            val workoutFragment = WorkoutFragment()
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                workoutFragment
            ).commit()
        }
    }

}