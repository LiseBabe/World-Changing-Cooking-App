package com.example.worldchangingcookingapp.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.UUID
import kotlin.time.Duration

data class Recipe(
    @DocumentId var id: String? = null,
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
    var preparationTime: Duration,
    var cookingTime: Duration,
    var restingTime: Duration,
    var cookingType: CookingType,
    var ingredients: List<Ingredients>,
    var steps: List<String>,
    var moreInformation: String,
)