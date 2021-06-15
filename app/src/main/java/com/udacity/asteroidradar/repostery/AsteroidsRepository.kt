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


class AsteroidsRepository(private val database: AsteroidsDatabase) {

    fun getAsteroidSelection(show: String): LiveData<List<ModelAsteroid>> {
        return when (show) {
            "all" -> { Transformations.map(database.asteroidDao.getAsteroids()) {
                it.asDomainModel()
            }
            }
            "today" -> { Transformations.map(database.asteroidDao.getAsteroidsToday(getToday())) {
                it.asDomainModel()
            }
            }
            else -> { Transformations.map(database.asteroidDao.getAsteroidsWeek(getSunday())) {
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
            database.asteroidDao.clear(getToday())
        }
    }

    fun getToday(): String {
        return SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
                .format(Calendar.getInstance().time)
    }

    fun getSunday(): String {
        var calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_WEEK, Calendar.DAY_OF_WEEK - Calendar.SUNDAY)
        val currentTime = calendar.time
        return SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
                .format(currentTime)
    }

}