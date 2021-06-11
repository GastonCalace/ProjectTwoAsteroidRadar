package com.udacity.asteroidradar.repostery


import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.database.PictureOfDayDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.ModelPictureOfDay
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class PictureOfDayRepository(private val database: PictureOfDayDatabase) {

    var picture: LiveData<ModelPictureOfDay> = Transformations.map(database.pictureOfDayDao.getPictureOfDay()) {
        it?.asDomainModel()
    }


    suspend fun refreshPicture() {
        withContext(Dispatchers.IO) {
            val picture = AsteroidApi.retrofitService.getPicture().await()
            database.pictureOfDayDao.insertPictureOfDay(picture.asDatabaseModel())

        }
    }
}