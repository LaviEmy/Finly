package com.example.finly.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import androidx.sqlite.db.SupportSQLiteDatabase


class AppDatabase {
    @Database(
        entities = [Transaction::class, Category::class],
        version = 1,
        exportSchema = false
    )
    @TypeConverters(Converters::class)
    abstract class AppDatabase : RoomDatabase() {

        abstract fun transactionDao(): TransactionDao
        abstract fun categoryDao(): CategoryDao

        companion object {
            @Volatile
            private var INSTANCE: AppDatabase? = null

            fun getDatabase(context: Context): AppDatabase {
                return INSTANCE ?: synchronized(this) {
                    Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "finance_database"
                    )
                        .addCallback(PrepopulateCallback())
                        .build().also { INSTANCE = it }
                }
            }
        }
    }

    // Заполняем дефолтные категории при первом запуске
    class PrepopulateCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Расходы
            db.execSQL("INSERT INTO categories (nameResId, isForIncome, isDefault) VALUES ('Groceries', 0, 1)")
            db.execSQL("INSERT INTO categories (nameResId, isForIncome, isDefault) VALUES ('Cafe', 0, 1)")
            db.execSQL("INSERT INTO categories (nameResId, isForIncome, isDefault) VALUES ('Shopping', 0, 1)")
            db.execSQL("INSERT INTO categories (nameResId, isForIncome, isDefault) VALUES ('Gasoline', 0, 1)")
            db.execSQL("INSERT INTO categories (nameResId, isForIncome, isDefault) VALUES ('Rent', 0, 1)")
            db.execSQL("INSERT INTO categories (nameResId, isForIncome, isDefault) VALUES ('Utilities', 0, 1)")
            db.execSQL("INSERT INTO categories (nameResId, isForIncome, isDefault) VALUES ('Subscription', 0, 1)")
            db.execSQL("INSERT INTO categories (nameResId, isForIncome, isDefault) VALUES ('Entertainment', 0, 1)")
            db.execSQL("INSERT INTO categories (nameResId, isForIncome, isDefault) VALUES ('Loan', 0, 1)")
            db.execSQL("INSERT INTO categories (nameResId, isForIncome, isDefault) VALUES ('Other', 0, 1)")
            // Доходы
            db.execSQL("INSERT INTO categories (nameResId, isForIncome, isDefault) VALUES ('Salary', 1, 1)")
            db.execSQL("INSERT INTO categories (nameResId, isForIncome, isDefault) VALUES ('Dividends', 1, 1)")
            db.execSQL("INSERT INTO categories (nameResId, isForIncome, isDefault) VALUES ('Gift', 1, 1)")
            db.execSQL("INSERT INTO categories (nameResId, isForIncome, isDefault) VALUES ('Other', 1, 1)")
        }
    }
}