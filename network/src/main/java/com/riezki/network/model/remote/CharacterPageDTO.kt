package com.riezki.network.model.remote

import com.riezki.network.model.domain.CharacterPage
import kotlinx.serialization.Serializable
import com.riezki.network.model.domain.Info

data class CharacterPageDTO(
    val info: InfoDTO?,
    val results: List<CharacterDTO>?
) {
    fun toDomainCharacterPage() : CharacterPage {
        return CharacterPage(
            info = Info(
                count = info?.count ?: 0,
                pages = info?.pages ?: 0,
                next = info?.next,
                prev = info?.prev
            ),
            characters = results?.map { it.toDomainCharacter() }
        )
    }
}

@Serializable
data class InfoDTO(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)
