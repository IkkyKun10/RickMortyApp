package com.riezki.rickmortyapp.di

import com.riezki.network.KtorClient
import com.riezki.rickmortyapp.repositories.CharacterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun providesKtorClient() : KtorClient {
        return KtorClient()
    }

    @Provides
    @Singleton
    fun providesCharacterRepository(ktorClient: KtorClient) : CharacterRepository {
        return CharacterRepository(ktorClient)
    }
}