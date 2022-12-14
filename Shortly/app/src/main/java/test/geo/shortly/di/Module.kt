package test.geo.shortly.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import test.geo.shortly.data.local.ShortlyDao
import test.geo.shortly.data.local.ShortlyDatabase
import test.geo.shortly.data.remote.ShortlyAPI
import test.geo.shortly.other.Constants.BASE_URL
import test.geo.shortly.other.Constants.DB_NAME
import test.geo.shortly.other.NetworkConnection
import test.geo.shortly.other.ShortlyUtil
import test.geo.shortly.repositories.ShortlyRepository
import test.geo.shortly.repositories.ShortlyRepositoryImpl
import test.geo.shortly.ui.LoadingView
import test.geo.shortly.ui.adapters.LinkHistoryAdapter
import javax.inject.Singleton

/*
* Represents Hilt singleton module class
* The @Singleton functions are defined to create dependencies which are later injected in required classes
* e.g., ShortlyRepositoryImpl is depended on instance of ShortlyDao and ShortlyAPI classes hence, singleton provider
* of the mentioned classes has been created here so that hilt can later inject the dependencies accordingly
* Dependencies are injected in singleton provider functions as well for example hilt provides the shortlyAPI in provideShortlyRepository
* function through provideShortlyAPI, here the first function doesn't have to annotate
* */

@Module
@InstallIn(SingletonComponent::class)
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

    @Singleton
    @Provides
    fun provideShortlyRepository(
        dao: ShortlyDao,
        api: ShortlyAPI
    ) = ShortlyRepositoryImpl(dao, api) as ShortlyRepository

    @Singleton
    @Provides
    fun provideNetworkInfo(
        @ApplicationContext context: Context
    ) = NetworkConnection(ShortlyUtil.checkNetworkConnection(context))

    @Singleton
    @Provides
    fun provideAdapter() = LinkHistoryAdapter()

    @Singleton
    @Provides
    fun provideLoadingView(
        @ApplicationContext context: Context
    ) = LoadingView.createLoading(context)

}