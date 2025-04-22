package com.example.worldchangingcookingapp.models

data class Recipe(
    var id: Long = 0L,
    var title: String,
    var publicationDate: String,
    var difficulty: Difficulty, //enum Difficulty
    var price: Price,
    var typeOfRecipe: TypeOfRecipe,
    var numberOfPeople: Double,
    var preparationTime: Double, //probably string ? see after
    var cookingTime: Double, //probably string ? see after
    var restingTime: Double? = null, //probably string ? see after
    var cookingType: CookingType,
    var posterPath: String,
    var ingredients: List<Ingredients>,
    var recipePreparation: String,
    var moreInformation: String,
)