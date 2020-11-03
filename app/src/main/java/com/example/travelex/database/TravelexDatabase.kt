package com.example.travelex.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Place::class], version = 1, exportSchema = false)
abstract class TravelexDatabase : RoomDatabase() {

    abstract val placeDao: PlaceDao

    private class TravelexDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let {
                scope.launch {
                    val placeDao = it.placeDao
                    placeDao.deleteAll()
                    placeDao.insert(
                        mutableListOf(
                            Place(
                                0,
                                "name1",
                                "description1",
                                "location1",
                                "photo1",
                                1,
                                "comment1",
                                "addInfo1"
                            ),
                            Place(
                                0,
                                "name2",
                                "description2",
                                "location2",
                                "photo2",
                                2,
                                "comment2",
                                "addInfo2"
                            ),
                            Place(
                                0,
                                "name3",
                                "description3",
                                "location3",
                                "photo3",
                                3,
                                "comment3",
                                "addInfo3"
                            ),
                        )
                    )
                }
            }
        }
    }

    companion object {

        @Volatile
        private var INSTANCE: TravelexDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): TravelexDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TravelexDatabase::class.java,
                    "travelex_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(TravelexDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance

            }
        }
    }

}