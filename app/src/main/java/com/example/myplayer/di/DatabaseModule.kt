package com.example.myplayer.di

import android.content.Context
import android.icu.text.IDNA
import com.example.myplayer.data.db.InfoDao
import com.example.myplayer.data.db.InfoDatabase
import com.example.myplayer.data.db.MoviesDao
import com.example.myplayer.data.db.MoviesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideMoviesDatabase(@ApplicationContext context: Context): MoviesDatabase {
        return MoviesDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideInfoDatabase(@ApplicationContext context: Context): InfoDatabase {
        return InfoDatabase.getInstance(context)
    }

    @Provides
    fun provideMoviesDao(database: MoviesDatabase): MoviesDao {
        return database.moviesDao()
    }

    @Provides
    fun provideInfoDao(database: InfoDatabase): InfoDao {
        return database.infoDao()
    }
}
