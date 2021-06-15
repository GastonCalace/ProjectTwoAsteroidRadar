package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabaseAsteroids
import com.udacity.asteroidradar.database.getDatabasePictures
import com.udacity.asteroidradar.repostery.AsteroidsRepository
import com.udacity.asteroidradar.repostery.PictureOfDayRepository
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters):
        CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val databaseAsteroids = getDatabaseAsteroids(applicationContext)
        val asteroidsRepository = AsteroidsRepository(databaseAsteroids)

        val databasePicture = getDatabasePictures(applicationContext)
        val pictureOfDayRepository = PictureOfDayRepository(databasePicture)

        return try {
            asteroidsRepository.refreshAsteroids()
            pictureOfDayRepository.refreshPicture()
            Result.success()
        } catch (exception: HttpException) {
            Result.retry()
        }
    }
}