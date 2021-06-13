package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {
    @Query("SELECT * FROM DatabaseAsteroid ORDER BY closeApproachDate ASC")
    fun getAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM DatabaseAsteroid WHERE closeApproachDate = :today ORDER BY closeApproachDate ASC LIMIT 1")
    fun getAsteroidsToday(today: String): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM DatabaseAsteroid ORDER BY closeApproachDate ASC")
    fun getAsteroidsWeek(): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)

    @Query("DELETE FROM databaseasteroid WHERE closeApproachDate < :today")
    fun clear(today: String)
}

@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidsDatabase

fun getDatabaseAsteroids(context: Context): AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java){
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                AsteroidsDatabase::class.java,
            "asteroids").build()
        }
    }
    return INSTANCE
}