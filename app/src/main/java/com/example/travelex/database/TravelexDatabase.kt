package com.example.travelex.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Place::class, PhotoModel::class], version = 1, exportSchema = false)
abstract class TravelexDatabase : RoomDatabase() {

    abstract val placeDao: PlaceDao
    abstract val photoModelDao: PhotoModelDao

    private class TravelexDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let {
                scope.launch {
                    val placeDao = it.placeDao
                    val photoModelDao = it.photoModelDao
                    placeDao.deleteAll()
                    photoModelDao.deleteAll()
                    placeDao.insert(
                        mutableListOf(
                            Place(
                                1,
                                0,
                                "Zhangye Danxia Geopark, China",
                                "Geology lovers and avid Instagrammers alike will be drawn to the otherworldly hues of the \"Rainbow Mountains.\" The colors were formed by the layering of sedimentary mineral deposits over millions of years, but it's hard to look at the flowing reds, yellows, and oranges and not feel like you're witnessing magic.",
                                "51.22229105088706, 22.606841115528717",
                                5f,
                                "Było super, polecam serdecznie <3"
                            ),
                            Place(
                                2,
                                0,
                                "Venice, Italy",
                                "If traversing the canals with a be-striped gondolier sounds unbearably touristy, stick to the sidewalks and spectacular arched bridges to get your fill of this truly unique, wildly romantic floating city.",
                                "51.23720139958699, 22.57654678717597",
                                4.8f,
                                "Ekstra, tylko zbyt wielu ludzi"
                            ),
                            Place(
                                3,
                                0,
                                "Banff National Park, Canada",
                                "The glacial lakes in Canada's first national park have some of the bluest water you've ever seen. Even if you're not particularly outdoorsy, you can still admire the views from one of the cozy and luxurious lakeside lodges throughout the park, like the Fairmont Chateau Lake Louise.",
                                "51.23152440824398, 22.568608979745317",
                                2.9f,
                                "Padał deszcz więc słaby wyjazd"
                            ),
                            Place(
                                4,
                                1,
                                "Banff National Park, Canada",
                                "The glacial lakes in Canada's first national park have some of the bluest water you've ever seen. Even if you're not particularly outdoorsy, you can still admire the views from one of the cozy and luxurious lakeside lodges throughout the park, like the Fairmont Chateau Lake Louise.",
                                "51.23152440824398, 22.568608979745317",
                                2.9f,
                                "Padał deszcz więc słaby wyjazd"
                            )
                        )
                    )
                    photoModelDao.insert(
                        mutableListOf(
                            PhotoModel(
                                0,
                                1,
                                "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/hbz-zhangye-gettyimages-175323801-1505334995.jpg"
                            ),
                            PhotoModel(
                                0,
                                2,
                                "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/hbz-venice-gettyimages-489741024-1505338894.jpg"
                            ),
                            PhotoModel(
                                0,
                                3,
                                "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/banff-517747003-1494616292.jpg"
                            ),
                            PhotoModel(
                                0,
                                1,
                                "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/banff-517747003-1494616292.jpg"
                            ),
                            PhotoModel(
                                0,
                                4,
                                "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/banff-517747003-1494616292.jpg"
                            )
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