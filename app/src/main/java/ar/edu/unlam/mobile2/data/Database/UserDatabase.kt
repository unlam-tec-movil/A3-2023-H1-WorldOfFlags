package ar.edu.unlam.mobile2.data.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Provides


@Database(entities =[UserEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase() {


    abstract fun userDao(): UserDao



}