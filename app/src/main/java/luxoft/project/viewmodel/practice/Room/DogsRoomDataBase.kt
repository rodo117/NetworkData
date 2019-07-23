package luxoft.project.viewmodel.practice.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import luxoft.project.viewmodel.practice.DataObjects.Dogs
import luxoft.project.viewmodel.practice.Interfaces.DogsDao

@Database(entities = arrayOf(Dogs::class), version = 1)
abstract class DogsRoomDataBase : RoomDatabase() {
    abstract fun dogsDao(): DogsDao

    companion object {

        @Volatile
        private var INSTANCE: DogsRoomDataBase? = null

        fun getDataBaseInstance(context: Context,scope: CoroutineScope): DogsRoomDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DogsRoomDataBase::class.java,
                    "Dogs_database"
                ).addCallback(DogsDataBaseCallBack(scope)).build()
                INSTANCE = instance
                return instance
            }

        }

    }

    private class DogsDataBaseCallBack(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.dogsDao())
                }
            }
        }


        suspend fun populateDatabase(dogDao: DogsDao) {
            dogDao.deleteAll()

            var dog = Dogs(dog="chihuahua")
            dogDao.insert(dog)
            dog = Dogs(dog="bulldog")
            dogDao.insert(dog)
        }
    }


}