package com.example.secondhiltapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.secondhiltapp.db.entity.*

@Database(
    entities = [SoccerNews::class,
        SoccerNewsChinese::class,
        LanguageData::class,
        IsClickable::class,
        BookMarkData::class
               ],
    version = 1
)
abstract class AppRoom: RoomDatabase() {

    abstract  fun getSoccerDao() : SoccerDao
    abstract  fun getSoccerChineseDao() : SoccerChineseDao
    abstract fun getLanguageDao() : LanguageDao
    abstract fun getIsActiveDao() : IsActiveDao
    abstract fun getBookmarkDao() : BookmarkDao

    companion object {
        private var DB_INSTANCE: AppRoom? = null

        fun getAppDBInstance(context: Context): AppRoom {
            if (DB_INSTANCE == null){
                DB_INSTANCE = Room.databaseBuilder(context.applicationContext,
                AppRoom::class.java,
                "SOCCER_DB")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return DB_INSTANCE!!
        }
    }
}