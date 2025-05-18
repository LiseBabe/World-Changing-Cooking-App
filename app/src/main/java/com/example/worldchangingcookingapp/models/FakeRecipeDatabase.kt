package com.example.worldchangingcookingapp.data

import com.example.worldchangingcookingapp.models.*
import com.google.firebase.Timestamp
import kotlin.time.Duration.Companion.minutes

object FakeRecipeDatabase {
    val recipes = listOf(
        Recipe(
            id = "1",
            title = "Pâtes Carbonara",
            authorId = "user123",
            authorName = "Jean Dupont",
            authorProfilePath = "",
            publicationDate = Timestamp.now(),
            description = "Une délicieuse recette italienne simple et rapide.",
            difficulty = Difficulty.MEDIUM,
            price = Price.MODERATE,
            typeOfRecipe = TypeOfRecipe.MAIN_COURSE,
            numberOfPeople = 2,
            preparationTime = 10,
            cookingTime = 15,
            restingTime = 0,
            cookingType = CookingType.STOVE,
            ingredients = listOf(
                Ingredients(200.0, "g", "Pâtes", ""),
                Ingredients(100.0, "g", "Lardons", ""),
                Ingredients(1.0, "pc", "Œuf", ""),
                Ingredients(50.0, "ml", "Crème", ""),
                Ingredients(1.0, "pincée", "Sel", "")
            ),
            steps = listOf("Faire cuire les pâtes", "Cuire les lardons", "Mélanger le tout"),
            moreInformation = "Ne pas utiliser de crème en version traditionnelle."
        ),
        Recipe(
            id = "2",
            title = "Salade César",
            authorId = "user456",
            authorName = "Marie Curie",
            authorProfilePath = "",
            publicationDate = Timestamp.now(),
            description = "Une salade classique avec poulet et sauce césar.",
            difficulty = Difficulty.EASY,
            price = Price.CHEAP,
            typeOfRecipe = TypeOfRecipe.STARTER,
            numberOfPeople = 4,
            preparationTime = 15,
            cookingTime = 10,
            restingTime = 0,
            cookingType = CookingType.NO_COOKING,
            ingredients = listOf(
                Ingredients(2.0, "pcs", "Blancs de poulet", ""),
                Ingredients(1.0, "pcs", "Laitue romaine", ""),
                Ingredients(100.0, "g", "Croûtons", ""),
                Ingredients(50.0, "g", "Parmesan", ""),
                Ingredients(100.0, "ml", "Sauce César", "")
            ),
            steps = listOf("Griller le poulet", "Mélanger les ingrédients", "Ajouter la sauce"),
            moreInformation = "Servir frais avec du pain grillé."
        ),
        Recipe(
            id = "3",
            title = "Gâteau au chocolat",
            authorId = "user789",
            authorName = "Luc Chocolatier",
            authorProfilePath = "",
            publicationDate = Timestamp.now(),
            description = "Moelleux, riche et fondant.",
            difficulty = Difficulty.HARD,
            price = Price.EXPENSIVE,
            typeOfRecipe = TypeOfRecipe.DESSERT,
            numberOfPeople = 6,
            preparationTime = 20,
            cookingTime = 30,
            restingTime = 60,
            cookingType = CookingType.OVEN,
            ingredients = listOf(
                Ingredients(200.0, "g", "Chocolat noir", ""),
                Ingredients(150.0, "g", "Beurre", ""),
                Ingredients(150.0, "g", "Sucre", ""),
                Ingredients(3.0, "pcs", "Œufs", ""),
                Ingredients(100.0, "g", "Farine", "")
            ),
            steps = listOf("Faire fondre le chocolat", "Mélanger les ingrédients", "Cuire au four"),
            moreInformation = "Laisser refroidir avant de déguster."
        ),
        Recipe(
            id = "4",
            title = "Quiche Lorraine",
            authorId = "user321",
            authorName = "Claire Boulanger",
            authorProfilePath = "",
            publicationDate = Timestamp.now(),
            description = "Une tarte salée traditionnelle à base de lardons et crème.",
            difficulty = Difficulty.MEDIUM,
            price = Price.MODERATE,
            typeOfRecipe = TypeOfRecipe.MAIN_COURSE,
            numberOfPeople = 4,
            preparationTime = 15,
            cookingTime = 40,
            restingTime = 10,
            cookingType = CookingType.OVEN,
            ingredients = listOf(
                Ingredients(1.0, "pcs", "Pâte brisée", ""),
                Ingredients(200.0, "g", "Lardons", ""),
                Ingredients(3.0, "pcs", "Œufs", ""),
                Ingredients(200.0, "ml", "Crème fraîche", ""),
                Ingredients(100.0, "g", "Fromage râpé", "")
            ),
            steps = listOf(
                "Étaler la pâte dans un moule",
                "Faire revenir les lardons",
                "Battre les œufs avec la crème",
                "Ajouter les lardons et le fromage",
                "Verser sur la pâte et enfourner"
            ),
            moreInformation = "Servir tiède avec une salade verte."
        ),
                Recipe(
                id = "5",
        title = "Smoothie Mangue Banane",
        authorId = "user654",
        authorName = "Alex Nutrition",
        authorProfilePath = "",
        publicationDate = Timestamp.now(),
        description = "Une boisson fruitée et rafraîchissante parfaite pour l'été.",
        difficulty = Difficulty.EASY,
        price = Price.CHEAP,
        typeOfRecipe = TypeOfRecipe.DRINK,
        numberOfPeople = 2,
        preparationTime = 5,
        cookingTime = 0,
        restingTime = 0,
        cookingType = CookingType.MICROWAVE,
        ingredients = listOf(
            Ingredients(1.0, "pcs", "Mangue", "Bien mûre"),
            Ingredients(1.0, "pcs", "Banane", ""),
            Ingredients(100.0, "ml", "Lait d'amande", ""),
            Ingredients(1.0, "c. à soupe", "Miel", ""),
            Ingredients(5.0, "pcs", "Glaçons", "")
        ),
        steps = listOf(
            "Peler la mangue et la banane",
            "Couper en morceaux",
            "Mixer avec le lait, le miel et les glaçons",
            "Servir immédiatement"
        ),
        moreInformation = "Peut être enrichi avec des graines de chia ou de lin."
    ),
        Recipe(
            id = "6",
            title = "Velouté de courgettes",
            authorId = "user901",
            authorName = "Emma Soupier",
            authorProfilePath = "",
            publicationDate = Timestamp.now(),
            description = "Une soupe douce et légère idéale en entrée ou dîner léger.",
            difficulty = Difficulty.EASY,
            price = Price.CHEAP,
            typeOfRecipe = TypeOfRecipe.STARTER,
            numberOfPeople = 4,
            preparationTime = 10,
            cookingTime = 20,
            restingTime = 0,
            cookingType = CookingType.STOVE,
            ingredients = listOf(
                Ingredients(3.0, "pcs", "Courgettes", "Moyennes"),
                Ingredients(1.0, "pcs", "Oignon", ""),
                Ingredients(1.0, "c. à soupe", "Huile d'olive", ""),
                Ingredients(500.0, "ml", "Bouillon de légumes", ""),
                Ingredients(2.0, "c. à soupe", "Crème fraîche", "")
            ),
            steps = listOf(
                "Faire revenir l'oignon dans l'huile d'olive",
                "Ajouter les courgettes coupées en morceaux",
                "Verser le bouillon, cuire 20 minutes",
                "Mixer puis ajouter la crème",
                "Servir chaud"
            ),
            moreInformation = "Peut être accompagné de croûtons ou d'un filet de citron."
        ),
        Recipe(
            id = "7",
            title = "Pancakes moelleux",
            authorId = "user555",
            authorName = "Léo Petitdej",
            authorProfilePath = "",
            publicationDate = Timestamp.now(),
            description = "Des pancakes épais et aérés pour un petit déjeuner gourmand. Des pancakes épais et aérés pour un petit déjeuner gourmand. Des pancakes épais et aérés pour un petit déjeuner gourmand. Des pancakes épais et aérés pour un petit déjeuner gourmand.",
            difficulty = Difficulty.MEDIUM,
            price = Price.CHEAP,
            typeOfRecipe = TypeOfRecipe.DESSERT,
            numberOfPeople = 4,
            preparationTime = 10,
            cookingTime = 15,
            restingTime = 10,
            cookingType = CookingType.STOVE,
            ingredients = listOf(
                Ingredients(200.0, "g", "Farine", ""),
                Ingredients(2.0, "pcs", "Œufs", ""),
                Ingredients(250.0, "ml", "Lait", ""),
                Ingredients(1.0, "sachet", "Levure chimique", ""),
                Ingredients(1.0, "c. à soupe", "Sucre", "")
            ),
            steps = listOf(
                "Mélanger tous les ingrédients secs",
                "Ajouter les œufs et le lait, fouetter",
                "Laisser reposer 10 minutes",
                "Cuire dans une poêle bien chaude",
                "Servir avec du sirop ou des fruits"
            ),
            moreInformation = "Parfaits avec du sirop d’érable ou des fruits rouges."
        )
    )
}
