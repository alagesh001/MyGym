package com.oxad.fitox.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.oxad.fitox.model.Member
import com.oxad.fitox.model.Workout

@Database(entities = [Member::class,Workout::class], version = 1, exportSchema = false)

abstract class AppDatabase : RoomDatabase() {
    abstract fun memberDao(): MemberDao
    abstract fun workoutDao(): WorkoutDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gym_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
   /*     // Migration from version 1 to 2 (Adding new "exercises" table)
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS exercises (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "name TEXT NOT NULL, " +
                            "category TEXT NOT NULL)"
                )
            }
        }*/
    }
}
