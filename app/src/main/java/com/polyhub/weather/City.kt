package com.polyhub.weather

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Entity
data class City(
    @PrimaryKey val id: UUID,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "lat") val lat: Double,
    @ColumnInfo(name = "lon") val lon: Double
)

@Dao
interface CityDao {
    @Query("SELECT * FROM city")
    fun getAll(): Flow<List<City>>

    @Query("SELECT * FROM city WHERE id IN (:cityId)")
    suspend fun loadById(cityId: Int): City

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg city: City)

    @Delete
    suspend fun delete(city: City)
}

@Database(entities = [City::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
}

