package com.example.finly.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.finly.data.model.Category
import com.example.finly.data.dao.CategoryDao
import com.example.finly.data.dao.DebtDao
import com.example.finly.data.dao.GoalDao
import com.example.finly.data.dao.SubscriptionDao
import com.example.finly.data.model.Transaction
import com.example.finly.data.dao.TransactionDao
import com.example.finly.data.model.Debt
import com.example.finly.data.model.Goal
import com.example.finly.data.model.Subscription

@Database(
    entities = [Transaction::class, Category::class, Goal::class, Debt::class, Subscription::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao

    abstract fun goalDao(): GoalDao
    abstract fun debtDao(): DebtDao
    abstract fun subscriptionDao(): SubscriptionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "finly_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(PrepopulateCallback())
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }

    class PrepopulateCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            db.execSQL("INSERT INTO categories (nameResId, isForIncome, isDefault, color) VALUES ('Groceries', 0, 1, '#e5b51e')")
            db.execSQL("INSERT INTO categories (nameResId, isForIncome, isDefault, color) VALUES ('Cafe', 0, 1, '#e5cf1e')")
            db.execSQL("INSERT INTO categories (nameResId, isForIncome, isDefault, color) VALUES ('Shopping', 0, 1, '#e377be')")
            db.execSQL("INSERT INTO categories (nameResId, isForIncome, isDefault, color) VALUES ('Gasoline', 0, 1, '#e377be')")
            db.execSQL("INSERT INTO categories (nameResId, isForIncome, isDefault, color) VALUES ('Rent', 0, 1, '#77e3bc')")
            db.execSQL("INSERT INTO categories (nameResId, isForIncome, isDefault, color) VALUES ('Utilities', 0, 1, '#e377a0')")
            db.execSQL("INSERT INTO categories (nameResId, isForIncome, isDefault, color) VALUES ('Subscription', 0, 1, '#77c7e3')")
            db.execSQL("INSERT INTO categories (nameResId, isForIncome, isDefault, color) VALUES ('Entertainment', 0, 1, '#40e9ee')")
            db.execSQL("INSERT INTO categories (nameResId, isForIncome, isDefault, color) VALUES ('Loan', 0, 1, '#d43555')")
            db.execSQL("INSERT INTO categories (nameResId, isForIncome, isDefault, color) VALUES ('Other', 0, 1, '#d435ce')")


            db.execSQL("INSERT INTO categories (nameResId, isForIncome, isDefault, color) VALUES ('Salary', 1, 1, '#34f103')")
            db.execSQL("INSERT INTO categories (nameResId, isForIncome, isDefault, color) VALUES ('Dividends', 1, 1, '#3ff410')")
            db.execSQL("INSERT INTO categories (nameResId, isForIncome, isDefault, color) VALUES ('Gift', 1, 1, '#53ee2b')")
            db.execSQL("INSERT INTO categories (nameResId, isForIncome, isDefault, color) VALUES ('Other', 1, 1, '#6dec4c')")
        }
    }
}