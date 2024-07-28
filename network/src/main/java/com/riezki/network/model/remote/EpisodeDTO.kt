package com.riezki.network.model.remote


import com.riezki.network.model.domain.Episode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeDTO(
    @SerialName("results")
    val results: List<EpisodeItemDTO>? = null
)

@Serializable
data class EpisodeItemDTO(
    @SerialName("air_date")
    val airDate: String? = null,
    @SerialName("characters")
    val characters: List<String?>? = null,
    @SerialName("created")
    val created: String? = null,
    @SerialName("episode")
    val episode: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("url")
    val url: String? = null
) {
    fun toEpisodeDomain() : Episode {
        return Episode(
            id = id,
            name = name,
            seasonNumber = episode?.filter { it.isDigit() }?.take(2)?.toInt(),
            episodeNumber = episode?.filter { it.isDigit() }?.takeLast(2)?.toInt(),
            airDate = airDate,
            characterIdsInEpisode = characters?.map {
                it?.substring(startIndex = it.lastIndexOf("/") + 1)?.toInt()
            }
        )
    }
}