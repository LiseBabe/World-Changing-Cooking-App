package com.example.worldchangingcookingapp.models

import java.util.UUID
import kotlin.time.Duration

data class Recipe(
    var id: UUID,
    var title: String,
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
    var posterPath: String,
    var ingredients: List<Ingredients>,
    var steps: List<String>,
    var moreInformation: String,
)