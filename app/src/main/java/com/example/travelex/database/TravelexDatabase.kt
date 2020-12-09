package com.example.travelex.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Place::class, PhotoModel::class, User::class], version = 1, exportSchema = false)
abstract class TravelexDatabase : RoomDatabase() {

    abstract val placeDao: PlaceDao
    abstract val photoModelDao: PhotoModelDao
    abstract val userDao: UserDao

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
                                "awdadawdawdawd",
                                "Zhangye Danxia Geopark, China",
                                "Gilberta Xóchitl",
                                "",
                                "https://cdn.pixabay.com/photo/2016/02/18/22/27/fog-1208283_1280.jpg",
                                "Geology lovers and avid Instagrammers alike will be drawn to the otherworldly hues of the \"Rainbow Mountains.\" The colors were formed by the layering of sedimentary mineral deposits over millions of years, but it's hard to look at the flowing reds, yellows, and oranges and not feel like you're witnessing magic.",
                                "51.22229105088706, 22.606841115528717",
                                5f,
                                "Było super, polecam serdecznie <3"
                            ),
                            Place(
                                2,
                                "awdadawdawdawd",
                                "Venice, Italy",
                                "Gionata Emma",
                                "",
                                "https://cdn.pixabay.com/photo/2016/01/19/17/48/woman-1149911_1280.jpg",
                                "If traversing the canals with a be-striped gondolier sounds unbearably touristy, stick to the sidewalks and spectacular arched bridges to get your fill of this truly unique, wildly romantic floating city.",
                                "51.23720139958699, 22.57654678717597",
                                4.8f,
                                "Ekstra, tylko zbyt wielu ludzi"
                            ),
                            Place(
                                3,
                                "awdadawdawdawd",
                                "Banff National Park, Canada",
                                "Pepe Isacco",
                                "",
                                "https://cdn.pixabay.com/photo/2016/03/09/10/23/model-1246028_1280.jpg",
                                "The glacial lakes in Canada's first national park have some of the bluest water you've ever seen. Even if you're not particularly outdoorsy, you can still admire the views from one of the cozy and luxurious lakeside lodges throughout the park, like the Fairmont Chateau Lake Louise.",
                                "51.23152440824398, 22.568608979745317",
                                2.9f,
                                "Padał deszcz więc słaby wyjazd"
                            ),
                            Place(
                                4,
                                "awdadawdawdawd",
                                "Banff National Park, Canada",
                                "Corona Azucena",
                                "",
                                "https://cdn.pixabay.com/photo/2015/07/31/17/52/suit-869380_1280.jpg",
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