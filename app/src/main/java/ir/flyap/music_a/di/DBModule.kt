package ir.flyap.music_a.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.flyap.music_a.db.AppDB
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Singleton
    @Provides
    fun provideAppDatabase(app: Application): AppDB {
        return Room.databaseBuilder(app, AppDB::class.java, "music.db").build()
    }
}

