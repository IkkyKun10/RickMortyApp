package com.riezki.network.model.remote

import com.riezki.network.model.domain.Character
import com.riezki.network.model.domain.CharacterGender
import com.riezki.network.model.domain.CharacterStatus
import com.riezki.network.model.domain.Location
import com.riezki.network.model.domain.Origin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class CharacterDTO(
    @SerialName("created")
    val created: String?,

    @SerialName("episode")
    val episode: List<String?>? = null,

    @SerialName("gender")
    val gender: String?,

    @SerialName("id")
    val id: Int?,

    @SerialName("image")
    val image: String?,

    @SerialName("location")
    val location: LocationDTO?,

    @SerialName("name")
    val name: String?,

    @SerialName("origin")
    val origin: OriginDTO?,

    @SerialName("species")
    val species: String?,

    @SerialName("status")
    val status: String?,

    @SerialName("type")
    val type: String?,

    @SerialName("url")
    val url: String?
) {
    fun toDomainCharacter() : Character {
        val characterGender = when (gender?.lowercase()) {
            "male" -> CharacterGender.Male
            "female" -> CharacterGender.Female
            "genderless" -> CharacterGender.Genderless
            else -> CharacterGender.Unknown
        }

        val characterStatus = when (status?.lowercase()) {
            "alive" -> CharacterStatus.Alive
            "dead" -> CharacterStatus.Dead
            else -> CharacterStatus.Unknown
        }

        return Character(
            created = created,
            episodeIds = episode?.map { it?.substring(it.lastIndexOf("/") + 1)?.toInt() ?: 0 },
            gender = characterGender,
            id = id,
            imageUrl = image,
            location = Location(location?.name, location?.url),
            name = name,
            origin = Origin(origin?.name, origin?.url),
            species = species,
            status = characterStatus,
            type = type
        )
    }
}

@Serializable
data class LocationDTO(
    @SerialName("name")
    val name: String?,
    @SerialName("url")
    val url: String?
)

@Serializable
data class OriginDTO(
    @SerialName("name")
    val name: String?,
    @SerialName("url")
    val url: String?
)
