package com.riezki.rickmortyapp.repositories

import com.riezki.network.KtorClient
import com.riezki.network.model.domain.Character
import com.riezki.network.model.domain.CharacterPage
import com.riezki.network.model.utils.ApiOperation
import javax.inject.Inject

/**
 * @author riezky maisyar
 */

class CharacterRepository @Inject constructor(
    private val ktorClient: KtorClient
) {
    suspend fun fetchCharacterPage(page: Int) : ApiOperation<CharacterPage> {
        return ktorClient.getCharacterByPage(page)
    }

    suspend fun fetchCharacter(characterId: Int) : ApiOperation<Character> {
        return ktorClient.getCharacter(characterId)
    }
}