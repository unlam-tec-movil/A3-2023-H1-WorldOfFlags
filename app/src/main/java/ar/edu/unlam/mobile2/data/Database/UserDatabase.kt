package ar.edu.unlam.mobile2.data.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Provides


@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)

abstract class UserDatabase : RoomDatabase() {


    abstract fun userDao(): UserDao


     companion object{
         @Volatile
         private var INSTANCE : UserDatabase ?= null

         fun getIntance(contex:Context):UserDatabase{
             val tempInstance= INSTANCE
             if(tempInstance!=null){
                 return tempInstance
             }
             synchronized(this){
                 val intance = Room.databaseBuilder(contex.applicationContext,UserDatabase::class.java,"user_database").build()
                 INSTANCE=intance
                 return intance
             }
         }
     }

}