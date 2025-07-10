package com.oxad.fitox.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.oxad.fitox.model.Member
import com.oxad.fitox.model.Workout

@Database(entities = [Member::class,Workout::class], version = 4, exportSchema = false)

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
                )
                   // .addMigrations(MIGRATION_2_3)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
        // Migration from version 1 to 2 (Adding new "exercises" table)
      /*  val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE members ADD COLUMN gymid INTEGER NOT NULL DEFAULT 0")

            }
        }*/
        fun clearDatabase(context: Context) {
            synchronized(this) {
                INSTANCE?.close()
                val result = context.deleteDatabase("gym_database")
                INSTANCE = null
                //Log.d("DatabaseClear", "Database cleared successfully: $result")
            }
        }
    }
}
