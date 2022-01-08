package uz.juo.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.juo.data.room.dao.BookDao
import uz.juo.data.room.entity.BookEntity

@Database(entities = [BookEntity::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun dao(): BookDao
}