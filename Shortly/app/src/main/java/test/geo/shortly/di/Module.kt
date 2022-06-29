package test.geo.shortly.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import test.geo.shortly.data.local.ShortlyDatabase
import test.geo.shortly.data.remote.ShortlyAPI
import test.geo.shortly.other.Constants.BASE_URL
import test.geo.shortly.other.Constants.DB_NAME
import javax.inject.Singleton

@Module
object Module {

    @Singleton
    @Provides
    fun provideShortlyDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, ShortlyDatabase::class.java, DB_NAME).build()

    @Singleton
    @Provides
    fun provideShortlyDao(
        database: ShortlyDatabase
    ) = database.shortlyDao()

    @Singleton
    @Provides
    fun provideShortlyAPI(): ShortlyAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ShortlyAPI::class.java)
    }

}