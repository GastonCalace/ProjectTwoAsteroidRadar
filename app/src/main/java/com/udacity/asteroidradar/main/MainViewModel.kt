package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.getDatabaseAsteroids
import com.udacity.asteroidradar.database.getDatabasePictures
import com.udacity.asteroidradar.repostery.AsteroidsRepository
import com.udacity.asteroidradar.repostery.PictureOfDayRepository
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val databaseAsteroids = getDatabaseAsteroids(application)
    private val asteroidsRepository = AsteroidsRepository(databaseAsteroids)

    private val databasePictureOfDay = getDatabasePictures(application)
    private val pictureOfDayRepository = PictureOfDayRepository(databasePictureOfDay)

    init {
        viewModelScope.launch {
            asteroidsRepository.refreshAsteroids()
            pictureOfDayRepository.refreshPicture()
        }
    }

    var asteroids = asteroidsRepository.asteroids
    var picture = pictureOfDayRepository.picture

}
