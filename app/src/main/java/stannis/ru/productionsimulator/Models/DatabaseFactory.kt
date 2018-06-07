package stannis.ru.productionsimulator.Models

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*
import java.sql.*


class DatabaseFactory(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "ProductionSimulatorDB") {

    companion object {
        private var instance: DatabaseFactory? = null

        @Synchronized
        fun getInstance(ctx: Context): DatabaseFactory {
            if (instance == null) {
                instance = DatabaseFactory(ctx.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable("Factories", true,
                "id" to INTEGER,
                "type" to INTEGER,
                "res" to INTEGER,
                "consumption" to INTEGER,
                "production" to INTEGER,
                "prod" to INTEGER)
        // "machine_stat" to )

        db.createTable("laborExchange", true,
                "name" to TEXT,
                "age" to INTEGER,
                "spec" to TEXT,
                "quality" to INTEGER,
                "nationality" to TEXT,
                "salary" to INTEGER,
                "dayOfBirth" to TEXT,
                "monthOfBirth" to TEXT
        )

        db.createTable("buy", true,
                "name" to TEXT,
                "id" to INTEGER,
                "price" to INTEGER)
        db.createTable("hiredStaff", true,
                "name" to TEXT,
                "age" to INTEGER,
                "spec" to TEXT,
                "quality" to INTEGER,
                "nationality" to TEXT,
                "salary" to INTEGER,
                "dayOfBirth" to TEXT,
                "monthOfBirth" to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    // Access property for Context
    val Context.factory: DatabaseFactory
        get() = DatabaseFactory.getInstance(applicationContext)

    // To manage factory DB

    fun addFactoryWithProperties(ctx: Context, id: Int, type: Int, res: Int, consumption: Int, production: Int, prod: Int) {
        getInstance(ctx).use {
            insert("Factories",
                    "id" to id, "type" to type, "res" to res, "consumption" to consumption,
                    "production" to production, "prod" to prod)
        }
    }

    fun setFactoryProperties(ctx: Context, id: Int, type: Int, res: Int, consumption: Int, production: Int, prod: Int) {
        getInstance(ctx).use {
            select("Factories")
                    .whereArgs("id = ${id}", "type" to type, "res" to res, "consumption" to consumption,
                            "production" to production, "prod" to prod)
        }
    }

    

    fun setStaffWithProperties(ctx: Context) {

    }

}

