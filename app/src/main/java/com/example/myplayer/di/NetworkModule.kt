package com.example.myplayer.di

import com.example.myplayer.api.MyPlayerService
import com.example.myplayer.api.TestServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module

class NetworkModule {
    @Singleton
    @Provides
    fun provideHttpApi(): MyPlayerService {
        return MyPlayerService.create()
    }

    @Singleton
    @Provides
    fun provideTestHttpApi(): TestServices {
        return TestServices.create()
    }

}