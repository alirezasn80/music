package ir.flyap.chavoshi.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.flyap.chavoshi.db.AppDB
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Singleton
    @Provides
    fun provideAppDatabase(app: Application): AppDB {
        val databaseFile = app.getDatabasePath("mydb.db")

        return if (databaseFile.exists()) {
            Room.databaseBuilder(app, AppDB::class.java, "mydb.db").build()

        } else {
            Room.databaseBuilder(app, AppDB::class.java, "mydb.db")
                .createFromAsset("database/mydb.db")
                .build()
        }

    }
}

