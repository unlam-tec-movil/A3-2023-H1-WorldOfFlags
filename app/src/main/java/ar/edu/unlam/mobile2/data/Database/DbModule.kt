package ar.edu.unlam.mobile2.data.Database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides
    @Singleton
    fun provide(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, UserDatabase::class.java, "user_database")
            .allowMainThreadQueries().fallbackToDestructiveMigration().build()


    @Provides
    @Singleton
    fun provideDao(db: UserDatabase) = db.userDao()

}