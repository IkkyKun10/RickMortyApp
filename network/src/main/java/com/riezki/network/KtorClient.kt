package com.riezki.network

import com.riezki.network.model.domain.Character
import com.riezki.network.model.domain.CharacterPage
import com.riezki.network.model.domain.Episode
import com.riezki.network.model.remote.CharacterDTO
import com.riezki.network.model.remote.CharacterPageDTO
import com.riezki.network.model.remote.EpisodeDTO
import com.riezki.network.model.remote.EpisodeItemDTO
import com.riezki.network.model.utils.ApiOperation
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * @author riezky maisyar
 */

class KtorClient() {
    private val client = HttpClient(OkHttp) {
        defaultRequest { url("https://rickandmortyapi.com/api/") }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    private var characterCache = mutableMapOf<Int, Character>()

    suspend fun getCharacter(id: Int): ApiOperation<Character> {
        characterCache[id]?.let { return ApiOperation.Success(it) }
        return safeApiCall {
            client.get("character/$id")
                .body<CharacterDTO>()
                .toDomainCharacter()
        }
    }

    suspend fun getCharacterByPage(pageNumber: Int) : ApiOperation<CharacterPage> {
        return safeApiCall {
            client.get("character/?page=$pageNumber")
                .body<CharacterPageDTO>()
                .toDomainCharacterPage()
        }
    }

    private suspend fun getEpisode(episodeId: Int): ApiOperation<Episode> {
        return safeApiCall {
            client.get("episode/$episodeId")
                .body<EpisodeItemDTO>()
                .toEpisodeDomain()
        }
    }

    suspend fun getEpisodes(episodeIds: List<Int>): ApiOperation<List<Episode>> {
        return if (episodeIds.size == 1) {
            getEpisode(episodeIds[0]).mapSuccess {
                listOf(it)
            }
        } else {
            val idsCommaSeparated = episodeIds.joinToString(separator = ",")
            safeApiCall {
                client.get("episode/$idsCommaSeparated")
                    .body<EpisodeDTO>().results
                    ?.map { it.toEpisodeDomain() } ?: emptyList()
            }
        }
    }

    private inline fun <T> safeApiCall(block: () -> T): ApiOperation<T> {
        return try {
            ApiOperation.Success(block())
        } catch (e: Exception) {
            ApiOperation.Failure(e)
        }
    }

}