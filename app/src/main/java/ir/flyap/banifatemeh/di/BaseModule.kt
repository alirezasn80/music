package ir.flyap.banifatemeh.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.flyap.banifatemeh.cache.datastore.DataStore
import ir.flyap.banifatemeh.cache.datastore.DataStoreManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BaseModule {

    @Singleton
    @Provides
    fun provideDataStoreManager(
        application: Application,
    ): DataStore = DataStoreManager(application)
}