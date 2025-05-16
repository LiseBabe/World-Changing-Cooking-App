package com.example.worldchangingcookingapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.worldchangingcookingapp.contants.CacheCategory
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import kotlin.time.Duration

//@Entity(tableName = "recipes")
//data class Recipe(
//    @DocumentId
//    @PrimaryKey
//    var id: String = "",
//    var title: String,
//    var authorId: String,
//    var authorName: String,
//    var authorProfilePath: String,
//    @ServerTimestamp
//    var publicationDate: Timestamp? = null,
//    var description: String,
//    var difficulty: Difficulty, //enum Difficulty
//    var price: Price,
//    var typeOfRecipe: TypeOfRecipe,
//    var numberOfPeople: Int,
//    var preparationTime: Duration,
//    var cookingTime: Duration,
//    var restingTime: Duration,
//    var cookingType: CookingType,
//    var ingredients: List<Ingredients>,
//    var steps: List<String>,
//    var moreInformation: String,
//    @get:Exclude
//    var cacheCategory : CacheCategory = CacheCategory.DRAFT
//)

@Entity(tableName = "recipes")
data class Recipe(
    @DocumentId
    @PrimaryKey
    var id: String = "",
    var title: String = "",
    var authorId: String = "",
    var authorName: String = "",
    var authorProfilePath: String = "",
    @ServerTimestamp
    var publicationDate: Timestamp? = null,
    var description: String = "",
    var difficulty: Difficulty = Difficulty.EASY,
    var price: Price = Price.CHEAP,
    var typeOfRecipe: TypeOfRecipe = TypeOfRecipe.OTHER,
    var numberOfPeople: Int = 0,
    var preparationTime: Long = 0L,
    var cookingTime: Long = 0L,
    var restingTime: Long = 0L,
    var cookingType: CookingType = CookingType.OTHER,
    var ingredients: List<Ingredients> = emptyList(),
    var steps: List<String> = emptyList(),
    var moreInformation: String = "",
    @get:Exclude
    var cacheCategory: CacheCategory = CacheCategory.DRAFT
)