package com.example.worldchangingcookingapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.worldchangingcookingapp.contants.CacheCategory
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import java.util.Date
import kotlin.time.Duration

@Serializable
@SerialName("Timestamp")
private data class TimestampSurrogate(val seconds: Long, val nanoseconds: Int)

object TimestampSerializer : KSerializer<Timestamp> {
    override val descriptor: SerialDescriptor = SerialDescriptor(
        "app.Timestamp",
        TimestampSurrogate.serializer().descriptor
        )

    override fun serialize(encoder: Encoder, value: Timestamp) {
        val surrogate = TimestampSurrogate(value.seconds, value.nanoseconds)
        encoder.encodeSerializableValue(TimestampSurrogate.serializer(), surrogate)
    }

    override fun deserialize(decoder: Decoder): Timestamp {
        val surrogate = decoder.decodeSerializableValue(TimestampSurrogate.serializer())
        return Timestamp(surrogate.seconds, surrogate.nanoseconds)
    }
}

@Serializable
@Entity(tableName = "recipes")
data class Recipe(
    @DocumentId
    @PrimaryKey
    var id: String = "",
    var title: String = "",
    var authorId: String = "",
    var authorName: String = "",
    var authorProfilePath: String = "",
    @Serializable(with = TimestampSerializer::class)
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