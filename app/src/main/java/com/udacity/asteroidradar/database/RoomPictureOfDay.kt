package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface PictureOfDayDao {
    @Query("select * from DatabasePictureOfDay")
    fun getPictureOfDay(): LiveData<DatabasePictureOfDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPictureOfDay(picture: DatabasePictureOfDay)
}

@Database(entities = [DatabasePictureOfDay::class], version = 1)
abstract class PictureOfDayDatabase : RoomDatabase() {
    abstract val pictureOfDayDao: PictureOfDayDao
}

private lateinit var INSTANCE: PictureOfDayDatabase

fun getDatabasePictures(context: Context): PictureOfDayDatabase {
    synchronized(AsteroidsDatabase::class.java){
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                PictureOfDayDatabase::class.java,
                "picture").build()
        }
    }
    return INSTANCE
}