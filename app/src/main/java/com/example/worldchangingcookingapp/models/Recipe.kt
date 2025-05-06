package com.example.worldchangingcookingapp.models

import com.google.firebase.firestore.DocumentId
import java.util.UUID
import kotlin.time.Duration

data class Recipe(
    @DocumentId var id: String,
    var title: String,
    var authorId: String,
    var authorName: String,
    var authorProfilePath: String,
    var publicationDate: String,
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
)