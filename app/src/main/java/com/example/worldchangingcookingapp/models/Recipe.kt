package com.example.worldchangingcookingapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.worldchangingcookingapp.contants.CacheCategory
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.util.UUID
import kotlin.time.Duration

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey
    @DocumentId
    var id: String? = null,
    var title: String,
    var authorId: String,
    var authorName: String,
    var authorProfilePath: String,
    @ServerTimestamp
    var publicationDate: Timestamp? = null,
    var description: String,
    var difficulty: Difficulty, //enum Difficulty
    var price: Price,
    var typeOfRecipe: TypeOfRecipe,
    var numberOfPeople: Int,
    var preparationTime: Duration, //probably string ? see after
    var cookingTime: Duration, //probably string ? see after
    var restingTime: Duration, //probably string ? see after
    var cookingType: CookingType,
    var ingredients: List<Ingredients>,
    var steps: List<String>,
    var moreInformation: String,
    @Exclude
    var cacheCategory : CacheCategory = CacheCategory.DRAFT
)