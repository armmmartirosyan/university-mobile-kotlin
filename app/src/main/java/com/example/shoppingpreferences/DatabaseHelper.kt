package com.example.shoppingpreferences

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    // Called only once, when the database file is created for the first time,
    // not on every app start (that is MainActivity.onCreate()).
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE)
    }

    // Called when DATABASE_VERSION is increased. Dropping the table deletes
    // all saved customers; that is fine for a lab, not for a real app.
    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CUSTOMERS")
        onCreate(db)
    }

    fun insertCustomer(name: String, email: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_EMAIL, email)
        }
        // Returns the new row's id, or -1L if the insert failed.
        return db.insert(TABLE_CUSTOMERS, null, values)
    }

    fun getAllCustomers(): Cursor {
        val db = readableDatabase
        // ORDER BY id DESC puts the newest customer at the top of the list.
        return db.rawQuery(
            "SELECT $COLUMN_NAME, $COLUMN_EMAIL " +
                "FROM $TABLE_CUSTOMERS ORDER BY $COLUMN_ID DESC",
            null
        )
    }

    companion object {
        private const val DATABASE_NAME = "shopping.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_CUSTOMERS = "customers"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_EMAIL = "email"

        private const val SQL_CREATE_TABLE = """
            CREATE TABLE $TABLE_CUSTOMERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_EMAIL TEXT NOT NULL
            )
        """
    }
}
