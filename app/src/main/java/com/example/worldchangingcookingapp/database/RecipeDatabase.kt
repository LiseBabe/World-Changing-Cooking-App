package com.example.worldchangingcookingapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.worldchangingcookingapp.contants.CacheCategory
import com.example.worldchangingcookingapp.models.CookingType
import com.example.worldchangingcookingapp.models.Difficulty
import com.example.worldchangingcookingapp.models.Ingredients
import com.example.worldchangingcookingapp.models.Price
import com.example.worldchangingcookingapp.models.Recipe
import com.example.worldchangingcookingapp.models.TypeOfRecipe
import com.google.firebase.Timestamp
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.sql.Types
import java.util.Date
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class Converters {
    @TypeConverter
    fun fromCacheCategory(value: CacheCategory?): String? {
        return value?.name
    }

    @TypeConverter
    fun toCacheCategory(value: String?): CacheCategory? {
        return CacheCategory.valueOf(value ?: CacheCategory.REGULAR.name)
    }

    @TypeConverter
    fun fromDifficulty(value: Difficulty?): String? {
        return value?.name
    }

    @TypeConverter
    fun toDifficulty(value: String?): Difficulty? {
        return Difficulty.valueOf(value ?: Difficulty.EASY.name)
    }

    @TypeConverter
    fun fromPrice(value: Price?): String? {
        return value?.name
    }

    @TypeConverter
    fun toPrice(value: String?): Price? {
        return Price.valueOf(value ?: Price.CHEAP.name)
    }

    @TypeConverter
    fun fromTypeOfRecipe(value: TypeOfRecipe?): String? {
        return value?.name
    }

    @TypeConverter
    fun toTypeOfRecipe(value: String?): TypeOfRecipe? {
        return TypeOfRecipe.valueOf(value ?: TypeOfRecipe.MAIN_COURSE.name)
    }

    @TypeConverter
    fun fromCookingType(value: CookingType?): String? {
        return value?.name
    }

    @TypeConverter
    fun toCookingType(value: String?): CookingType? {
        return CookingType.valueOf(value ?: CookingType.OTHER.name)
    }

    @TypeConverter
    fun toDuration(value: Int?): Duration? {
        return value?.seconds
    }

    @TypeConverter
    fun fromDuration(value: Duration?): Int? {
        return value?.inWholeSeconds!!.toInt()
    }

    @TypeConverter
    fun fromIngredients(ingredients: Ingredients?): String? {
        return ingredients?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun toIngredients(ingredientsJson: String?): Ingredients? {
        return ingredientsJson?.let { Json.decodeFromString<Ingredients>(it) }
    }

    @TypeConverter
    fun fromIngredientsList(value: List<Ingredients>?): String? {
        return value?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun toIngredientsList(value: String?): List<Ingredients>? {
        val adapter = Json.serializersModule.serializer<List<Ingredients>>()

        return value?.let { Json.decodeFromString(adapter, it) }
    }

    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return list?.joinToString(separator = "||") // Use a delimiter unlikely to be in your strings
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.split("||")?.map { it } // Split by the same delimiter
    }

    @TypeConverter
    fun fromTimestamp(value: Timestamp?): Long? {
        return value?.toDate()?.time
    }

    @TypeConverter
    fun toTimestamp(value: Long?): Timestamp? {
        return value?.let {
            Timestamp(Date(it))
        }
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