package com.udacity.asteroidradar.repostery


import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.asDatabaseModel
import com.udacity.asteroidradar.domain.ModelAsteroid
import com.udacity.asteroidradar.network.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.SUNDAY

class AsteroidsRepository(private val database: AsteroidsDatabase) {

    val date = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            .format(Calendar.getInstance().time)


    var asteroids: LiveData<List<ModelAsteroid>> = Transformations.map(database.asteroidDao.getAsteroids()) {
        it.asDomainModel()
    }

    fun getAsteroidSelection(day: Int = 0): LiveData<List<ModelAsteroid>> {
        return when (day) {
            0 -> { Transformations.map(database.asteroidDao.getAsteroids()) {
                    it.asDomainModel()
                }
            }
            (Calendar.DAY_OF_WEEK) -> { Transformations.map(database.asteroidDao.getAsteroidsToday(date)) {
                    it.asDomainModel()
                }
            }
            else -> { Transformations.map(database.asteroidDao.getAsteroidsWeek()) {
                    it.asDomainModel()
                }
            }
        }
    }


    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroidsList = AsteroidApi.retrofitService.getNetworkAsteroids().await()
            database.asteroidDao.insertAll(*parseAsteroidsJsonResult(
                    JSONObject(asteroidsList))
                    .asDatabaseModel())
            database.asteroidDao.clear(date)
        }
    }
}