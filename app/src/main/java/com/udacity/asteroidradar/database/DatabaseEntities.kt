package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.domain.ModelAsteroid
import com.udacity.asteroidradar.domain.ModelPictureOfDay

@Entity
data class DatabaseAsteroid constructor(
        @PrimaryKey
        val id: Long,
        val codename: String,
        val closeApproachDate: String,
        val absoluteMagnitude: Double,
        val estimatedDiameter: Double,
        val relativeVelocity: Double,
        val distanceFromEarth: Double,
        val isPotentiallyHazardous: Boolean)

@Entity
data class DatabasePictureOfDay constructor(
        @PrimaryKey
        val mediaType: String,
        val title: String,
        val url: String
)

fun List<DatabaseAsteroid>.asDomainModel(): List<ModelAsteroid> {
    return map {
        ModelAsteroid(
                id = it.id,
                codename = it.codename,
                closeApproachDate = it.closeApproachDate,
                absoluteMagnitude = it.absoluteMagnitude,
                estimatedDiameter = it.estimatedDiameter,
                relativeVelocity = it.relativeVelocity,
                distanceFromEarth = it.distanceFromEarth,
                isPotentiallyHazardous = it.isPotentiallyHazardous)
    }
}

fun DatabasePictureOfDay.asDomainModel(): ModelPictureOfDay {
        return ModelPictureOfDay(
                mediaType = this.mediaType,
                title = this.title,
                url = this.url
        )
}