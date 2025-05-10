package com.example.worldchangingcookingapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.worldchangingcookingapp.contants.CacheCategory
import com.example.worldchangingcookingapp.models.Recipe

class Converters {
    @TypeConverter
    fun fromCacheCategory(value: CacheCategory?): String? {
        return value?.name
    }

    @TypeConverter
    fun toCacheCategory(value: String?): CacheCategory? {
        return CacheCategory.valueOf(value ?: CacheCategory.REGULAR.name)
    }
}


@Database(entities = [Recipe::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDataAccessObject

    companion object {
        @Volatile
        private var Instance: RecipeDatabase? = null

        fun getDatabase(context: Context): RecipeDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context,
                    RecipeDatabase::class.java, "recipe_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}