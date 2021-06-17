package com.udacity.asteroidradar.main

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.MenuItem
import androidx.lifecycle.*
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.getDatabaseAsteroids
import com.udacity.asteroidradar.database.getDatabasePictures
import com.udacity.asteroidradar.domain.ModelAsteroid
import com.udacity.asteroidradar.repostery.AsteroidsRepository
import com.udacity.asteroidradar.repostery.PictureOfDayRepository
import kotlinx.coroutines.launch
import java.util.*
import java.util.Calendar.SUNDAY


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val databaseAsteroids = getDatabaseAsteroids(application)
    private val asteroidsRepository = AsteroidsRepository(databaseAsteroids)

    private val databasePictureOfDay = getDatabasePictures(application)
    private val pictureOfDayRepository = PictureOfDayRepository(databasePictureOfDay)

    private val _navigateToSelectedAsteroid = MutableLiveData<ModelAsteroid>()
    val navigateToSelectedAsteroid : LiveData<ModelAsteroid>
        get() = _navigateToSelectedAsteroid

    private val filter = MutableLiveData<String>("all")
    val asteroids = Transformations.switchMap(filter) {
        asteroidsRepository.getAsteroidSelection(it)
    }

    init {
        viewModelScope.launch {
            if(isNetworkAvailable(application)){
                asteroidsRepository.refreshAsteroids()
                pictureOfDayRepository.refreshPicture()
            }
        }
    }

    var picture = pictureOfDayRepository.picture

    fun updateFilter(item: MenuItem) {
        when (item.itemId) {
            R.id.show_all_menu -> filter.value = "all"
            R.id.show_today_menu -> filter.value = "today"
            else -> filter.value = "else"
        }
    }

    fun displayAsteroidDetails(modelAsteroid: ModelAsteroid) {
        _navigateToSelectedAsteroid.value = modelAsteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }
}
