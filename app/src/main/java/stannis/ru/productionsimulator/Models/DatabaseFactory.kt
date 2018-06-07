package stannis.ru.productionsimulator.Models

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class DatabaseFactory(ctx : Context) : ManagedSQLiteOpenHelper(ctx, "ProductionSimulatorDB") {

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
        db.createTable("Factories", false,
                "id" to INTEGER,
                "type" to INTEGER,
                "res" to INTEGER,
                "consumption" to INTEGER,
                "productivity" to INTEGER,
                "production" to INTEGER)
               // "machine_stat" to )

        db.createTable("laborExchange", false,
                "name" to TEXT,
                "age" to INTEGER,
                "spec" to TEXT,
                "class" to INTEGER,
                "nationality" to TEXT,
                "salary" to INTEGER)

        db.createTable("buy", false,
                "name" to TEXT,
                "id" to INTEGER,
                "price" to INTEGER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    // Access property for Context
    val Context.factory: DatabaseFactory
        get() = DatabaseFactory.getInstance(applicationContext)

    // To manage factory DB

    fun addFactoryWithProperties(ctx : Context, id : Int, type : Int, res : Int, consumption : Int, productivity : Int, production : Int) {
        getInstance(ctx).use {
            insert("Factories",
                    "id" to id, "type" to type, "res" to res, "consumption" to consumption,
                            "production" to productivity, "prod" to production)
        }
    }

    // TODO Write SELECT

    fun getFactoryProperties(ctx : Context, id : Int, type : Int, res : Int, consumption : Int, productivity : Int, production : Int) {
        getInstance(ctx).use {
            select("Factories")
                    .whereArgs("id = $id").column("type")
        }
    }

    // TODO Write UPDATE

    /*

    fun setFactoryProperties(ctx : Context, id : Int, type : Int, res : Int, consumption : Int, productivity : Int, production : Int) {
        getInstance(ctx).use {
            update()
        }
    }

    */
}